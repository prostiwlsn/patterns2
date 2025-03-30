package com.example.h_bankpro.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.h_bankpro.domain.entity.CredentialsEntity
import com.example.h_bankpro.domain.entity.TokenEntity
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AuthorizationLocalStorage(context: Context) : IAuthorizationLocalRepository {
    private val credentials = MutableStateFlow(CredentialsEntity())

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs = EncryptedSharedPreferences.create(
        "auth_prefs_pro",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun updateCredentials(update: CredentialsEntity.() -> CredentialsEntity) {
        credentials.update { credentials.value.update() }
    }

    override fun getCredentialsFlow() = credentials

    override fun getCredentialsState() = credentials.value

    override suspend fun getToken(): TokenEntity? {
        val accessToken = prefs.getString("access_token_pro", null)
        val refreshToken = prefs.getString("refresh_token_pro", null)
        val expiresAt = prefs.getLong("expires_at_pro", -1).takeIf { it != -1L }
        return if (accessToken != null && refreshToken != null && expiresAt != null) {
            TokenEntity(accessToken, refreshToken, expiresAt)
        } else null
    }

    override suspend fun saveToken(token: TokenEntity) {
        prefs.edit()
            .putString("access_token_pro", token.accessToken)
            .putString("refresh_token_pro", token.refreshToken)
            .putLong("expires_at_pro", token.expiresAt ?: -1L)
            .apply()
    }

    override suspend fun clearToken() {
        prefs.edit()
            .remove("access_token_pro")
            .remove("refresh_token_pro")
            .remove("expires_at_pro")
            .apply()
    }

    override fun reset() = credentials.update { CredentialsEntity() }
}