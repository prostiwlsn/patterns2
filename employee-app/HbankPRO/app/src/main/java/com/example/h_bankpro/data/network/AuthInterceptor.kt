package com.example.h_bankpro.data.network

import com.example.h_bankpro.data.dto.RefreshRequestDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(
    private val localRepository: IAuthorizationLocalRepository,
    private val remoteRepository: IAuthorizationRemoteRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val tokens = runBlocking { localRepository.getToken() }

        val accessToken = tokens?.accessToken ?: return chain.proceed(originalRequest)

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            response.close()

            val refreshToken = tokens.refreshToken
            if (refreshToken != null) {
                val refreshResult = runBlocking {
                    remoteRepository.refreshToken(RefreshRequestDto(refreshToken))
                }

                when (refreshResult) {
                    is RequestResult.Success -> {
                        val newAccessToken = refreshResult.data.accessToken
                        val newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                        return chain.proceed(newRequest)
                    }
                    else -> {
                        runBlocking { localRepository.clearToken() }
                        return response
                    }
                }
            } else {
                runBlocking { localRepository.clearToken() }
                return response
            }
        }

        return response
    }

    private fun Request.Builder.header(name: String, value: String): Request.Builder {
        removeHeader(name)
        addHeader(name, value)
        return this
    }
}