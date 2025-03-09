package com.example.h_bank.data.repository.authorization

import com.example.h_bank.domain.entity.authorization.AuthorizationCommand
import com.example.h_bank.domain.repository.authorization.IAuthorizationCommandStorage
import kotlinx.coroutines.flow.MutableSharedFlow

class AuthorizationCommandStorage(): IAuthorizationCommandStorage {
    private val commands = MutableSharedFlow<AuthorizationCommand?>()

    override suspend fun pushCommand(command: AuthorizationCommand) = commands.emit(command)

    override fun getCommandsFlow() = commands
}