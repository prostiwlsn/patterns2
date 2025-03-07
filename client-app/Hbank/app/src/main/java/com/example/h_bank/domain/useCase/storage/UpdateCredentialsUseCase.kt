package com.example.h_bank.domain.useCase.storage

class UpdateCredentialsUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke(update: CredentialsEntity.() -> CredentialsEntity) =
        storageRepository.updateCredentials(update)
}