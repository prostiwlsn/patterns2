package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.domain.entity.CredentialsEntity
import com.example.h_bankpro.domain.entity.TokenEntity
import kotlinx.coroutines.flow.Flow

interface IAuthorizationLocalRepository {
    fun updateCredentials(update: CredentialsEntity.() -> CredentialsEntity)
    fun getCredentialsFlow(): Flow<CredentialsEntity>
    fun getCredentialsState(): CredentialsEntity
    suspend fun getToken(): TokenEntity?
    suspend fun saveToken(token: TokenEntity)
    suspend fun clearToken()
    fun reset()
}