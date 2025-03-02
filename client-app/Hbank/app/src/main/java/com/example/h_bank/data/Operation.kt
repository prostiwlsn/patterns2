package com.example.h_bank.data

import java.time.LocalDateTime
import java.util.UUID

data class Operation(
    val id: UUID,
    val senderAccountId: UUID,
    val recipientAccountId: UUID,
    val transactionDateTime: LocalDateTime,
    val amount: Float,
    val message: String,
    val operationType: OperationType,
)