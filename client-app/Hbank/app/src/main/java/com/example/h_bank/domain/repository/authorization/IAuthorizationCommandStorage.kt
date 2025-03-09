package com.example.h_bank.domain.repository.authorization

import com.example.h_bank.domain.entity.authorization.AuthorizationCommand
import kotlinx.coroutines.flow.Flow

interface IAuthorizationCommandStorage {
    suspend fun pushCommand(command: AuthorizationCommand)

    fun getCommandsFlow(): Flow<AuthorizationCommand?>
}