package com.example.h_bank.domain.model

import com.example.h_bank.data.dto.payment.OperationTypeDto
import kotlinx.datetime.LocalDateTime

data class OperationShort(
    val id: String,
    val amount: Float,
    val directionToMe: Boolean,
    val transactionDateTime: LocalDateTime,
    val operationType: OperationTypeDto
)