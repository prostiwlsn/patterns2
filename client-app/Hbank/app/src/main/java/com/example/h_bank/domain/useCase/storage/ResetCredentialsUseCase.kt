package com.example.h_bank.domain.useCase.storage

import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository

class ResetCredentialsUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke() = storageRepository.reset()
}