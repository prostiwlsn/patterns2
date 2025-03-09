package com.example.h_bank.domain.useCase.storage

import com.example.h_bank.domain.entity.authorization.CredentialsEntity
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository

class UpdateCredentialsUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke(update: CredentialsEntity.() -> CredentialsEntity) =
        storageRepository.updateCredentials(update)
}