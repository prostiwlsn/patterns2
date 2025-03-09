package com.example.h_bankpro.domain.model

import com.example.h_bankpro.data.OperationType
import kotlinx.datetime.LocalDateTime

data class Operation(
    val id: String,
    val senderAccountId: String?,
    val senderAccountNumber: String?,
    val recipientAccountId: String?,
    val recipientAccountNumber: String?,
    val directionToMe: Boolean,
    val amount: Float,
    val transactionDateTime: LocalDateTime,
    val message: String?,
    val operationType: OperationType,
)