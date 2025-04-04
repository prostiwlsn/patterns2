package com.example.h_bankpro.data.dto

import com.example.h_bankpro.data.OperationType
import com.example.h_bankpro.domain.model.Operation
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
data class OperationDto(
    val id: String,
    val senderAccountId: String?,
    val senderAccountNumber: String?,
    val recipientAccountId: String?,
    val recipientAccountNumber: String?,
    val directionToMe: Boolean,
    val amount: Double,
    val transactionDateTime: Instant,
    val message: String?,
    val operationType: OperationType,
    val isPaymentExpired: Boolean?
)

internal fun OperationDto.toDomain(): Operation =
    Operation(
        id = id,
        senderAccountId = senderAccountId,
        senderAccountNumber = senderAccountNumber,
        recipientAccountId = recipientAccountId,
        recipientAccountNumber = recipientAccountNumber,
        directionToMe = directionToMe,
        amount = amount,
        transactionDateTime = transactionDateTime.toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime(),
        message = message,
        operationType = operationType,
        isPaymentExpired = isPaymentExpired
    )