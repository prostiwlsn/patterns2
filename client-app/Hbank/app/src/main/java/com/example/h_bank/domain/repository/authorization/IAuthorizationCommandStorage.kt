package com.example.h_bank.domain.repository.authorization

import com.example.h_bank.domain.entity.authorization.Command
import kotlinx.coroutines.flow.Flow

interface IAuthorizationCommandStorage {
    suspend fun pushCommand(command: Command)

    fun getCommandsFlow(): Flow<Command?>
}