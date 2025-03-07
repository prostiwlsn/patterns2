package com.example.h_bank.domain.useCase.storage

class GetCredentialsFlowUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke() = storageRepository.getCredentialsFlow()
}