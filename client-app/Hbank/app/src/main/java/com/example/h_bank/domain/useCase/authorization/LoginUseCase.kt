package com.example.h_bank.domain.useCase.authorization

import com.example.h_bank.domain.repository.authorization.IAuthorizationRemoteRepository

class LoginUseCase(
    private val authorizationRepository: IAuthorizationRemoteRepository,
) {
    suspend operator fun invoke() = authorizationRepository.login()
}
