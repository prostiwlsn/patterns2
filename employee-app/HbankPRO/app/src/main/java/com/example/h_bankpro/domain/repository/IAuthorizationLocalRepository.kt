package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.domain.entity.CredentialsEntity
import kotlinx.coroutines.flow.Flow

interface IAuthorizationLocalRepository {
    fun updateCredentials(update: CredentialsEntity.() -> CredentialsEntity)
    fun getCredentialsFlow(): Flow<CredentialsEntity>
    fun getCredentialsState(): CredentialsEntity
}