package com.example.h_bank.data.repository.authorization

import com.example.h_bank.data.dataSource.local.AuthorizationLocalDataSource
import com.example.h_bank.domain.entity.authorization.CredentialsEntity
import com.example.h_bank.domain.entity.authorization.TokenEntity
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
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

    override fun setUserId(userId: String) {
        credentials.update { it.copy(userId = userId) }
    }

    override fun getUserId() = credentials.value.userId
}