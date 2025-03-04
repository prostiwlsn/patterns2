package com.example.h_bankpro.presentation.transactionInfo

import com.example.h_bankpro.data.Operation
import com.example.h_bankpro.data.OperationType
import java.time.LocalDateTime
import java.util.UUID

data class TransactionInfoState(
    val operation: Operation = Operation(
        id = UUID.randomUUID(),
        senderAccountId = UUID.randomUUID(),
        recipientAccountId = UUID.randomUUID(),
        transactionDateTime = LocalDateTime.now(),
        amount = 1000.0f,
        message = "Перевод на карту",
        operationType = OperationType.TRANSFER
    )
)
