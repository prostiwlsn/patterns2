package com.example.h_bankpro.domain.model

import com.example.h_bankpro.data.OperationType
import kotlinx.datetime.LocalDateTime

data class OperationShort(
    val id: String,
    val amount: Float,
    val directionToMe: Boolean,
    val transactionDateTime: LocalDateTime,
    val operationType: OperationType
)