package com.example.h_bank.data.repository.authorization

import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.repository.authorization.IAuthorizationCommandStorage
import kotlinx.coroutines.flow.MutableSharedFlow

class AuthorizationCommandStorage(): IAuthorizationCommandStorage {
    private val commands = MutableSharedFlow<Command?>()

    override suspend fun pushCommand(command: Command) = commands.emit(command)

    override fun getCommandsFlow() = commands
}