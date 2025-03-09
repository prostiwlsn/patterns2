package com.example.h_bankpro.data.repository

import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.repository.ICommandStorage
import kotlinx.coroutines.flow.MutableSharedFlow

class CommandStorage: ICommandStorage {
    private val commands = MutableSharedFlow<Command>()

    override suspend fun pushCommand(command: Command) = commands.emit(command)

    override fun getCommands() = commands
}