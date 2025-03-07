package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.mapper.toRegisterRequestDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.IAuthorizationRemoteRepository

class RegisterUseCase(
    private val authorizationRepository: IAuthorizationRemoteRepository,
    private val storageRepository: IAuthorizationLocalRepository,
) {
    suspend operator fun invoke(): RequestResult<TokenDto> {
        val request = storageRepository.getCredentialsState().toRegisterRequestDto()

        return authorizationRepository.register(request)
    }
}