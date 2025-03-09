package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.domain.entity.Command
import kotlinx.coroutines.flow.Flow

interface ICommandStorage {
    suspend fun pushCommand(command: Command)

    fun getCommands(): Flow<Command>
}