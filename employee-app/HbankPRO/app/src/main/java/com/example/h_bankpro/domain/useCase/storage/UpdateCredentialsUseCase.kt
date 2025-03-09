package com.example.h_bankpro.domain.useCase.storage

import com.example.h_bankpro.domain.entity.CredentialsEntity
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository

class UpdateCredentialsUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke(update: CredentialsEntity.() -> CredentialsEntity) =
        storageRepository.updateCredentials(update)
}