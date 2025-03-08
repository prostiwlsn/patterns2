package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.RefreshRequestDto
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.network.TokenApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.entity.TokenEntity
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.ITokenRepository

class TokenRepository(
    private val localRepository: IAuthorizationLocalRepository,
    private val tokenApi: TokenApi
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
        val currentToken = localRepository.getToken()
        val accessToken =
            currentToken?.accessToken ?: return RequestResult.Error(-1, "No access token available")

        val result = runResultCatching {
            tokenApi.refreshToken(
                request = RefreshRequestDto(refreshToken),
                accessToken = "Bearer $accessToken"
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