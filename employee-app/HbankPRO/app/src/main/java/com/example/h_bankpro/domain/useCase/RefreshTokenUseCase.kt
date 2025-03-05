package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.RefreshRequestDto
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.ITokenStorage

class RefreshTokenUseCase(
    private val remoteRepository: IAuthorizationRemoteRepository,
    private val tokenStorage: ITokenStorage
) {
    suspend operator fun invoke(): RequestResult<TokenDto> {
        val refreshToken = tokenStorage.getTokenState().refreshToken
            ?: return RequestResult.Error(null, "No refresh token available")
        return remoteRepository.refreshToken(RefreshRequestDto(refreshToken))
    }
}