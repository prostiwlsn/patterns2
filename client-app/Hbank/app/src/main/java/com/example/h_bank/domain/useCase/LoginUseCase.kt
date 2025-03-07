package com.example.h_bank.domain.useCase

import com.example.h_bank.domain.repository.IAuthorizationRemoteRepository

class LoginUseCase(
    private val authorizationRepository: IAuthorizationRemoteRepository,
) {
    suspend operator fun invoke() = authorizationRepository.login()
}
