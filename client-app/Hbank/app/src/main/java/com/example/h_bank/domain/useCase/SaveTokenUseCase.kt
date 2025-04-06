package com.example.h_bank.domain.useCase

import com.example.h_bank.domain.entity.authorization.TokenEntity
import com.example.h_bank.domain.repository.authorization.ITokenRepository

class SaveTokenUseCase(
    private val tokenRepository: ITokenRepository
) {
    suspend operator fun invoke(accessToken: String?, refreshToken: String?) {
        if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
            val expiresAt = parseExpiresAtFromToken(accessToken) ?: (System.currentTimeMillis() / 1000 + 3600)
            val tokenEntity = TokenEntity(accessToken, refreshToken, expiresAt)
            tokenRepository.saveToken(tokenEntity)
        }
    }

    private fun parseExpiresAtFromToken(token: String): Long? {
        return try {
            val payload = token.split(".")[1]
            val decodedPayload = String(android.util.Base64.decode(payload, android.util.Base64.URL_SAFE))
            val json = org.json.JSONObject(decodedPayload)
            json.optLong("exp").takeIf { it > 0 }
        } catch (e: Exception) {
            null
        }
    }
}