package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.dataSource.remote.TokenRemoteDataSource
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.authorization.TokenEntity
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.authorization.ITokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenRepository(
    private val localRepository: IAuthorizationLocalRepository,
    private val remoteDataSource: TokenRemoteDataSource
) : ITokenRepository {
    override suspend fun getToken(): TokenEntity? = withContext(Dispatchers.IO) {
        return@withContext localRepository.getToken()
    }

    override suspend fun saveToken(token: TokenEntity) = withContext(Dispatchers.IO) {
        localRepository.saveToken(token)
    }

    override suspend fun clearToken() = withContext(Dispatchers.IO) {
        localRepository.clearToken()
    }

    override suspend fun refreshToken(refreshToken: String): RequestResult<TokenDto> {
        return remoteDataSource.refreshToken(refreshToken)
    }
}