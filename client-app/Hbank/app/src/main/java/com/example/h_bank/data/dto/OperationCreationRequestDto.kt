package com.example.h_bank.data.dto

import com.example.h_bank.data.OperationType
import kotlinx.serialization.Serializable

@Serializable
data class OperationCreationRequestDto(
    val senderAccountId: String?,
    val recipientAccountId: String,
    val amount: Double,
    val message: String?,
    val operationType: OperationType,
)