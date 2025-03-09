package com.example.h_bank.presentation.paymentHistory.model

import com.example.h_bank.data.dto.payment.OperationTypeDto
import kotlinx.datetime.LocalDateTime

data class OperationShortModel(
    val id: String,
    val amount: Float,
    val directionToMe: Boolean,
    val transactionDateTime: LocalDateTime,
    val operationType: OperationTypeDto
)