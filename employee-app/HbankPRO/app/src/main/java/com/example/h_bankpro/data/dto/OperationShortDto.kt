package com.example.h_bankpro.data.dto

import com.example.h_bankpro.data.OperationType
import com.example.h_bankpro.domain.model.OperationShort
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant

import kotlinx.serialization.Serializable
import java.time.ZoneId

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
        transactionDateTime = transactionDateTime.toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime(),
        operationType = operationType
    )