package com.example.h_bank.data.dto

import com.example.h_bank.data.Operation
import com.example.h_bank.data.OperationType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class OperationDto(
    val id: String,
    val senderAccountId: String?,
    val recipientAccountId: String,
    val directionToMe: Boolean,
    val amount: Float,
    val transactionDateTime: Instant,
    val message: String?,
    val operationType: OperationType,
)

internal fun OperationDto.toDomain(): Operation =
    Operation(
        id = id,
        senderAccountId = senderAccountId,
        recipientAccountId = recipientAccountId,
        directionToMe = directionToMe,
        amount = amount,
        transactionDateTime = transactionDateTime.toLocalDateTime(TimeZone.currentSystemDefault()),
        message = message,
        operationType = operationType
    )