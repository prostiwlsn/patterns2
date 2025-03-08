package com.example.h_bank.domain.useCase

import TokenEntity
import com.example.h_bank.data.dto.RefreshRequestDto
import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bank.domain.repository.ITokenRepository
import com.example.h_bank.domain.repository.ITokenStorage

class RefreshTokenUseCase(
    private val localRepository: IAuthorizationLocalRepository,
    private val tokenRepository: ITokenRepository,
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