package com.example.h_bankpro.data.network

import com.example.h_bankpro.data.dto.OperationShortDto
import kotlinx.coroutines.flow.Flow

interface OperationWebSocketApi {
    fun connect(accountId: String): Flow<OperationShortDto>
    fun disconnect()
}