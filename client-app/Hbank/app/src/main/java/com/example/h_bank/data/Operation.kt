package com.example.h_bank.data

import com.example.h_bank.data.dto.payment.OperationTypeDto
import java.time.LocalDateTime

data class Operation(
    val id: String,
    val senderAccountId: String?,
    val recipientAccountId: String,
    val directionToMe: Boolean,
    val amount: Double,
    val transactionDateTime: LocalDateTime,
    val message: String?,
    val operationType: OperationTypeDto,
    val isPaymentExpired: Boolean?,
)