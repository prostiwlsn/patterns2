package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dataSource.local.AuthorizationLocalDataSource
import com.example.h_bankpro.data.dataSource.remote.TokenRemoteDataSource
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.entity.TokenEntity
import com.example.h_bankpro.domain.repository.ITokenRepository

class TokenRepository(
    private val localDataSource: AuthorizationLocalDataSource,
    private val remoteDataSource: TokenRemoteDataSource
) : ITokenRepository {
    override suspend fun getToken(): TokenEntity? = localDataSource.getToken()

    override suspend fun saveToken(token: TokenEntity) = localDataSource.saveToken(token)

    override suspend fun clearToken() = localDataSource.clearToken()

    override suspend fun refreshToken(refreshToken: String): RequestResult<TokenDto> {
        val result = remoteDataSource.refreshToken(refreshToken)
        return when (result) {
            is RequestResult.Success -> {
                localDataSource.saveToken(TokenEntity.fromTokenDto(result.data))
                result
            }
            is RequestResult.Error -> result
            is RequestResult.NoInternetConnection -> result
        }
    }
}