package com.example.h_bank.data.network

import com.example.h_bank.data.dto.payment.OperationShortDto
import kotlinx.coroutines.flow.Flow

interface OperationWebSocketApi {
    fun connect(accountId: String): Flow<OperationShortDto>
    fun disconnect()
}