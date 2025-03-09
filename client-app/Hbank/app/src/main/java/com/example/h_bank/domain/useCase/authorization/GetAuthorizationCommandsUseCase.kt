package com.example.h_bank.domain.useCase.authorization

import com.example.h_bank.domain.repository.authorization.IAuthorizationCommandStorage

class GetAuthorizationCommandsUseCase(
    private val storage: IAuthorizationCommandStorage,
) {
    operator fun invoke() = storage.getCommandsFlow()
}