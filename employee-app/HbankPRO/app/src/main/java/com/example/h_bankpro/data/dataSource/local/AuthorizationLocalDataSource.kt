package com.example.h_bankpro.data.dataSource.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.h_bankpro.domain.entity.TokenEntity

class AuthorizationLocalDataSource(
    context: Context
) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs = EncryptedSharedPreferences.create(
        "auth_prefs_pro",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun getToken(): TokenEntity? {
        val accessToken = prefs.getString("access_token_pro", null)
        val refreshToken = prefs.getString("refresh_token_pro", null)
        val expiresAt = prefs.getLong("expires_at_pro", -1).takeIf { it != -1L }
        return if (accessToken != null && refreshToken != null && expiresAt != null) {
            TokenEntity(accessToken, refreshToken, expiresAt)
        } else null
    }

    suspend fun saveToken(token: TokenEntity) {
        prefs.edit()
            .putString("access_token_pro", token.accessToken)
            .putString("refresh_token_pro", token.refreshToken)
            .putLong("expires_at_pro", token.expiresAt ?: -1L)
            .apply()
    }

    suspend fun clearToken() {
        prefs.edit()
            .remove("access_token_pro")
            .remove("refresh_token_pro")
            .remove("expires_at_pro")
            .apply()
    }
}