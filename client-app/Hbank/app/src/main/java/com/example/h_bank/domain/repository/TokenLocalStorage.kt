package com.example.h_bank.domain.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.auth0.android.jwt.JWT
import com.example.h_bank.domain.entity.TokenEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TokenLocalStorage(context: Context) : ITokenStorage {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPrefs = EncryptedSharedPreferences.create(
        "auth_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _token = MutableStateFlow(loadToken())
    override fun getTokenFlow() = _token.asStateFlow()

    override fun saveToken(token: TokenEntity) {
        val expiresAt = token.accessToken?.let { accessToken ->
            try {
                val jwt = JWT(accessToken)
                jwt.expiresAt?.time
            } catch (e: Exception) {
                token.expiresAt
            }
        } ?: token.expiresAt

        sharedPrefs.edit()
            .putString("access_token", token.accessToken)
            .putString("refresh_token", token.refreshToken)
            .putLong("expires_at", expiresAt ?: 0L)
            .apply()
        _token.value = token.copy(expiresAt = expiresAt)
    }

    override fun getTokenState(): TokenEntity = _token.value

    override fun clearToken() {
        sharedPrefs.edit()
            .remove("access_token")
            .remove("refresh_token")
            .remove("expires_at")
            .apply()
        _token.value = TokenEntity()
    }

    private fun loadToken(): TokenEntity {
        val accessToken = sharedPrefs.getString("access_token", null)
        val refreshToken = sharedPrefs.getString("refresh_token", null)
        val expiresAt = sharedPrefs.getLong("expires_at", 0L).takeIf { it > 0 }
        return TokenEntity(accessToken, refreshToken, expiresAt)
    }

    override fun isTokenValid(): Boolean {
        val token = getTokenState()
        val expiresAt = token.expiresAt
        return token.accessToken != null && (expiresAt != null && expiresAt > System.currentTimeMillis())
    }
}