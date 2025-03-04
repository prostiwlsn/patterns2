package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository

class LoginUseCase(
    private val authorizationRepository: IAuthorizationRemoteRepository,
) {
    suspend operator fun invoke() = authorizationRepository.login()
}
