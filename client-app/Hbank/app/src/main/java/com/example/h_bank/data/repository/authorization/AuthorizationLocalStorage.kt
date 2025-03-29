package com.example.h_bank.data.repository.authorization

import com.example.h_bank.domain.entity.authorization.TokenEntity
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.h_bank.domain.entity.authorization.CredentialsEntity
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AuthorizationLocalStorage(context: Context) : IAuthorizationLocalRepository {
    private val credentials = MutableStateFlow(CredentialsEntity())

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs = EncryptedSharedPreferences.create(
        "auth_prefs",
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
        val accessToken = prefs.getString("access_token", null)
        val refreshToken = prefs.getString("refresh_token", null)
        val expiresAt = prefs.getLong("expires_at", -1).takeIf { it != -1L }
        return if (accessToken != null && refreshToken != null && expiresAt != null) {
            TokenEntity(accessToken, refreshToken, expiresAt)
        } else null
    }

    override suspend fun saveToken(token: TokenEntity) {
        prefs.edit()
            .putString("access_token", token.accessToken)
            .putString("refresh_token", token.refreshToken)
            .putLong("expires_at", token.expiresAt ?: -1L)
            .apply()
    }

    override suspend fun clearToken() {
        prefs.edit()
            .remove("access_token")
            .remove("refresh_token")
            .remove("expires_at")
            .apply()
    }

    override fun reset() = credentials.update { CredentialsEntity() }

    override fun setUserId(userId: String) {
        credentials.update { it.copy(userId = userId) }
    }

    override fun getUserId() = credentials.value.userId
}