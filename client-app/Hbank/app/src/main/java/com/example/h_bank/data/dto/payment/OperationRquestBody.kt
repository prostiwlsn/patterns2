package com.example.h_bank.data.dto.payment

import kotlinx.serialization.Serializable

@Serializable
data class OperationRquestBody(
    val senderAccountId: String?,
    val recipientAccountId: String,
    val amount: Double,
    val message: String?,
    val operationType: OperationTypeDto,
)