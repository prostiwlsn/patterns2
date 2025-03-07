package com.example.h_bank.domain.useCase.storage

class ResetCredentialsUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke() = storageRepository.reset()
}