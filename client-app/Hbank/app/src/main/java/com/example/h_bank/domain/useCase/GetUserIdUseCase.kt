package com.example.h_bank.domain.useCase

import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository

class GetUserIdUseCase(
    private val storageRepository: IAuthorizationLocalRepository
) {
    operator fun invoke() = storageRepository.getUserId()
}