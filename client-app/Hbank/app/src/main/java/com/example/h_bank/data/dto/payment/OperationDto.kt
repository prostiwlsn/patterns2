package com.example.h_bank.data.dto.payment

import com.example.h_bank.data.Operation
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class OperationDto(
    val id: String,
    val senderAccountId: String?,
    val recipientAccountId: String,
    val directionToMe: Boolean,
    val amount: Double,
    val transactionDateTime: Instant,
    val message: String?,
    val operationType: OperationTypeDto,
)

internal fun OperationDto.toDomain(): Operation =
    Operation(
        id = id,
        senderAccountId = senderAccountId,
        recipientAccountId = recipientAccountId,
        directionToMe = directionToMe,
        amount = amount,
        transactionDateTime = LocalDateTime.now(),
        message = message,
        operationType = operationType
    )