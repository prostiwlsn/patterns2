package com.example.h_bank.data.network

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.repository.authorization.ITokenRepository
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(
    private val tokenRepository: ITokenRepository,
    private val pushCommandUseCase: PushCommandUseCase,
) : Interceptor {
    companion object {
        private const val MAX_RETRY_ATTEMPTS = 1
    }

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        handleRequest(chain, chain.request(), 0)
    }

    private suspend fun handleRequest(
        chain: Interceptor.Chain,
        originalRequest: Request,
        attempt: Int
    ): Response {
        if (originalRequest.url.encodedPath.endsWith("/refresh")) {
            return chain.proceed(originalRequest)
        }

        val tokens = tokenRepository.getToken()
        val accessToken = tokens?.accessToken

        if (accessToken == null || !requiresAuth(originalRequest.url.toString())) {
            return chain.proceed(originalRequest)
        }

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401 && attempt < MAX_RETRY_ATTEMPTS) {
            response.close()

            val refreshToken = tokens.refreshToken
            if (refreshToken != null) {
                when (val refreshResult = tokenRepository.refreshToken(refreshToken)) {
                    is RequestResult.Success -> {
                        val newAccessToken = refreshResult.data.accessToken
                        val newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                        return handleRequest(chain, newRequest, attempt + 1)
                    }

                    else -> {
                        tokenRepository.clearToken()
                        return response
                    }
                }
            } else {
                tokenRepository.clearToken()
                return response
            }
        }

        return response
    }

    private fun requiresAuth(url: String): Boolean {
        return !url.contains("/login") && !url.contains("/register") && !url.contains("/refresh")
    }
}