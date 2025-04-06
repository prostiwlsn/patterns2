package com.example.h_bank.data.repository.authorization

import com.example.h_bank.data.dto.RegisterDto
import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.dataSource.remote.AuthorizationRemoteDataSource
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.authorization.IAuthorizationRemoteRepository

class AuthorizationRemoteRepository(
    private val remoteDataSource: AuthorizationRemoteDataSource
) : IAuthorizationRemoteRepository {
    override suspend fun login(): RequestResult<TokenDto> {
        return remoteDataSource.login()
    }

    override suspend fun register(request: RegisterDto): RequestResult<TokenDto> {
        return remoteDataSource.register(request)
    }

    override suspend fun logout(): RequestResult<Unit> {
        return remoteDataSource.logout()
    }
}