package com.example.h_bank.data.repository

import com.example.h_bank.domain.entity.CredentialsEntity
import com.example.h_bank.domain.entity.TokenEntity
import com.example.h_bank.domain.repository.IAuthorizationLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AuthorizationLocalStorage : IAuthorizationLocalRepository {
    private val credentials = MutableStateFlow(CredentialsEntity())

    override fun updateCredentials(update: CredentialsEntity.() -> CredentialsEntity) {
        credentials.update { credentials.value.update() }
    }

    override fun getCredentialsFlow() = credentials

    override fun getCredentialsState() = credentials.value

    override fun reset() = credentials.update { CredentialsEntity() }
}