package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dataSource.local.AuthorizationLocalDataSource
import com.example.h_bankpro.domain.entity.CredentialsEntity
import com.example.h_bankpro.domain.entity.TokenEntity
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AuthorizationLocalRepository(
    private val localDataSource: AuthorizationLocalDataSource
) : IAuthorizationLocalRepository {
    private val credentials = MutableStateFlow(CredentialsEntity())

    override fun updateCredentials(update: CredentialsEntity.() -> CredentialsEntity) {
        credentials.update { credentials.value.update() }
    }

    override fun getCredentialsFlow() = credentials

    override fun getCredentialsState() = credentials.value

    override suspend fun getToken(): TokenEntity? {
        return localDataSource.getToken()
    }

    override suspend fun saveToken(token: TokenEntity) {
        localDataSource.saveToken(token)
    }

    override suspend fun clearToken() {
        localDataSource.clearToken()
    }

    override fun reset() = credentials.update { CredentialsEntity() }
}