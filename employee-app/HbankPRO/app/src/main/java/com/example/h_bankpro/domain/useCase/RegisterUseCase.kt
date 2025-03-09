package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.mapper.toRegisterRequestDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository

class RegisterUseCase(
    private val authorizationRepository: IAuthorizationRemoteRepository,
    private val storageRepository: IAuthorizationLocalRepository,
) {
    suspend operator fun invoke(): RequestResult<TokenDto> {
        val request = storageRepository.getCredentialsState().toRegisterRequestDto()

        return authorizationRepository.register(request)
    }
}