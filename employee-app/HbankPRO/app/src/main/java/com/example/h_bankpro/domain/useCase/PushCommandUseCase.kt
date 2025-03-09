package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.repository.ICommandStorage

class PushCommandUseCase(
    private val storage: ICommandStorage,
) {
    suspend operator fun invoke(command: Command) = storage.pushCommand(command)
}