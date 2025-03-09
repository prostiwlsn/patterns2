package com.example.h_bankpro.domain.useCase.storage

import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository

class ResetCredentialsUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke() = storageRepository.reset()
}