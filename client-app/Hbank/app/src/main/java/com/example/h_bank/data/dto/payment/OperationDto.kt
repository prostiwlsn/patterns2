package com.example.h_bank.data.dto.payment

import com.example.h_bank.data.Operation
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable
import java.time.ZoneId

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
    val isPaymentExpired: Boolean?,
)

internal fun OperationDto.toDomain(): Operation =
    Operation(
        id = id,
        senderAccountId = senderAccountId,
        recipientAccountId = recipientAccountId,
        directionToMe = directionToMe,
        amount = amount,
        transactionDateTime = transactionDateTime.toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime(),
        message = message,
        operationType = operationType,
        isPaymentExpired = isPaymentExpired
    )