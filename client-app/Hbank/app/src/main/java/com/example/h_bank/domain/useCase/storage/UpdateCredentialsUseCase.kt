package com.example.h_bank.domain.useCase.storage

import com.example.h_bank.domain.entity.CredentialsEntity
import com.example.h_bank.domain.repository.IAuthorizationLocalRepository

class UpdateCredentialsUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke(update: CredentialsEntity.() -> CredentialsEntity) =
        storageRepository.updateCredentials(update)
}