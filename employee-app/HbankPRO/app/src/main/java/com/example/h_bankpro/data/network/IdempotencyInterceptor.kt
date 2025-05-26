package com.example.h_bankpro.data.network

import okhttp3.Interceptor
import okhttp3.Response

class IdempotencyInterceptor(
    private val keyManager: IdempotencyKeyManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var idempotencyKey = keyManager.getIdempotencyKey(originalRequest)
        var attempt = 1
        val maxAttempts = 2

        while (attempt <= maxAttempts) {
            val requestWithKey = originalRequest.newBuilder()
                .header("X-Idempotency-Key", idempotencyKey)
                .build()

            val response = chain.proceed(requestWithKey)
            if (response.code == 409) {
                keyManager.clearKey(originalRequest)
                idempotencyKey = keyManager.getIdempotencyKey(originalRequest)
                attempt++
                continue
            }
            return response
        }

        throw RuntimeException("Failed after $maxAttempts attempts due to idempotency key conflict")
    }
}