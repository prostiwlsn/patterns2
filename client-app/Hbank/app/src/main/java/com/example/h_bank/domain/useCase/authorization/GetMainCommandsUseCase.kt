package com.example.h_bank.domain.useCase.authorization

import com.example.h_bank.domain.repository.authorization.IAuthorizationCommandStorage

class GetMainCommandsUseCase(
    private val storage: IAuthorizationCommandStorage,
) {
    operator fun invoke() = storage.getCommandsFlow()
}