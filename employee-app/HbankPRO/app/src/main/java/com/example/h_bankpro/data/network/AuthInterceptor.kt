package com.example.h_bankpro.data.network

import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.ITokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenRepository: ITokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val tokens = runBlocking { tokenRepository.getToken() }

        val accessToken = tokens?.accessToken
        if (accessToken == null || !requiresAuth(originalRequest.url.toString())) {
            return chain.proceed(originalRequest)
        }

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401 && !originalRequest.url.toString().contains("/refresh")) {
            response.close()

            val refreshToken = tokens.refreshToken
            if (refreshToken != null) {
                val refreshResult = runBlocking { tokenRepository.refreshToken(refreshToken) }

                when (refreshResult) {
                    is RequestResult.Success -> {
                        val newAccessToken = refreshResult.data.accessToken
                        val newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                        return chain.proceed(newRequest)
                    }
                    else -> {
                        runBlocking { tokenRepository.clearToken() }
                        return response
                    }
                }
            } else {
                runBlocking { tokenRepository.clearToken() }
                return response
            }
        }

        return response
    }

    private fun requiresAuth(url: String): Boolean {
        return !url.contains("/login") && !url.contains("/register") && !url.contains("/refresh")
    }
}