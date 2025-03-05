package com.example.h_bankpro.data.network

import com.example.h_bankpro.domain.repository.ITokenStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStorage: ITokenStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenStorage.getTokenState().accessToken

        val modifiedRequest = originalRequest.newBuilder()
            .apply {
                token?.let {
                    header("Authorization", "Bearer $it")
                }
            }
            .build()

        return chain.proceed(modifiedRequest)
    }
}