package com.example.h_bank.domain.useCase.authorization

import com.example.h_bank.domain.entity.authorization.AuthorizationCommand
import com.example.h_bank.domain.repository.authorization.IAuthorizationCommandStorage

class PushAuthorizationCommandUseCase(
    private val storage: IAuthorizationCommandStorage,
) {
    suspend operator fun invoke(command: AuthorizationCommand) = storage.pushCommand(command)
}