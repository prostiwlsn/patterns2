package com.example.h_bank.domain.useCase.authorization

import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.repository.authorization.IAuthorizationCommandStorage

class PushCommandUseCase(
    private val storage: IAuthorizationCommandStorage,
) {
    suspend operator fun invoke(command: Command) = storage.pushCommand(command)
}