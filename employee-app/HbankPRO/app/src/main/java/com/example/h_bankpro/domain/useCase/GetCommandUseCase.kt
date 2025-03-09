package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.domain.repository.ICommandStorage

class GetCommandUseCase(
    private val storage: ICommandStorage,
) {
    operator fun invoke() = storage.getCommands()
}