package com.example.h_bankpro.domain.useCase.storage

import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository

class GetCredentialsFlowUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke() = storageRepository.getCredentialsFlow()
}