package com.example.h_bank.data.repository

import TokenEntity
import com.example.h_bank.data.dto.RefreshRequestDto
import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.network.TokenApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.ITokenRepository

class TokenRepository(
    private val localRepository: IAuthorizationLocalRepository,
    private val tokenApi: TokenApi,
) : ITokenRepository {
    override suspend fun getToken(): TokenEntity? {
        return localRepository.getToken()
    }

    override suspend fun saveToken(token: TokenEntity) {
        localRepository.saveToken(token)
    }

    override suspend fun clearToken() {
        localRepository.clearToken()
    }

    override suspend fun refreshToken(refreshToken: String): RequestResult<TokenDto> {
        val result = runResultCatching {
            tokenApi.refreshToken(
                request = RefreshRequestDto(refreshToken),
                accessToken = "Bearer $refreshToken"
            )
        }
        return when (result) {
            is RequestResult.Success -> {
                localRepository.saveToken(TokenEntity.fromTokenDto(result.data))
                result
            }

            is RequestResult.Error -> result
            is RequestResult.NoInternetConnection -> result
        }
    }
}