package com.example.h_bank.data.dto

import com.example.h_bank.data.dto.payment.OperationTypeDto
import kotlinx.serialization.Serializable

@Serializable
data class OperationRequestBody(
    val senderAccountId: String?,
    val recipientAccountId: String,
    val amount: Double,
    val message: String?,
    val operationType: OperationTypeDto,
)