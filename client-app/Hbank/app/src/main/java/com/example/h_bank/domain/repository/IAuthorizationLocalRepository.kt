package com.example.h_bank.domain.repository

import TokenEntity
import com.example.h_bank.domain.entity.CredentialsEntity
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