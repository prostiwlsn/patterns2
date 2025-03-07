package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.RefreshRequestDto
import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.ITokenStorage

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