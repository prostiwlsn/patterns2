package com.example.h_bank.data.dto

import com.example.h_bank.data.OperationType
import com.example.h_bank.domain.model.OperationShort
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
    val operationType: OperationType
)

internal fun OperationShortDto.toDomain(): OperationShort =
    OperationShort(
        id = id,
        amount = amount,
        directionToMe = directionToMe,
        transactionDateTime = transactionDateTime.toLocalDateTime(TimeZone.currentSystemDefault()),
        operationType = operationType
    )