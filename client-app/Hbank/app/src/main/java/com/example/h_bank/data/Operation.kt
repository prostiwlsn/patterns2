package com.example.h_bank.data

import java.time.LocalDateTime

data class Operation(
    val id: String,
    val senderAccountId: String?,
    val recipientAccountId: String,
    val directionToMe: Boolean,
    val amount: Float,
    val transactionDateTime: LocalDateTime,
    val message: String?,
    val operationType: OperationType,
)