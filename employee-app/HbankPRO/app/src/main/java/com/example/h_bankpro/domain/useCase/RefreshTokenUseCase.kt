package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.entity.TokenEntity
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.ITokenRepository

class RefreshTokenUseCase(
    private val localRepository: IAuthorizationLocalRepository,
    private val tokenRepository: ITokenRepository
) {
    suspend operator fun invoke(): RequestResult<TokenEntity> {
        val tokens = localRepository.getToken()
            ?: return RequestResult.Error(401, "No tokens found")

        return if (tokens.isExpired()) {
            val refreshToken = tokens.refreshToken
                ?: return RequestResult.Error(401, "Refresh token is missing")

            when (val result = tokenRepository.refreshToken(refreshToken)) {
                is RequestResult.Success -> {
                    val newTokenEntity = TokenEntity.fromTokenDto(result.data)
                    localRepository.saveToken(newTokenEntity)
                    RequestResult.Success(newTokenEntity)
                }

                is RequestResult.Error -> {
                    localRepository.clearToken()
                    RequestResult.Error(result.code, result.message ?: "Failed to refresh token")
                }

                is RequestResult.NoInternetConnection -> {
                    localRepository.clearToken()
                    RequestResult.NoInternetConnection()
                }
            }
        } else {
            RequestResult.Success(tokens)
        }
    }
}