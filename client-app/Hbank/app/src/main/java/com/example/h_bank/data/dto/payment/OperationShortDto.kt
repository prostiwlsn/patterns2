package com.example.h_bank.data.dto.payment

import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class OperationShortDto(
    val id: String,
    val amount: Float,
    val directionToMe: Boolean,
    val transactionDateTime: Instant,
    val operationType: OperationTypeDto
)

internal fun OperationShortDto.toDomain(): OperationShortModel =
    OperationShortModel(
        id = id,
        amount = amount,
        directionToMe = directionToMe,
        transactionDateTime = transactionDateTime.toLocalDateTime(TimeZone.currentSystemDefault()),
        operationType = operationType
    )