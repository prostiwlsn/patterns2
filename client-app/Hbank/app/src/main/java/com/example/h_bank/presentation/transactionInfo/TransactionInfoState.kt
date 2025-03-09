package com.example.h_bank.presentation.transactionInfo

import com.example.h_bank.data.Operation
import com.example.h_bank.data.dto.payment.OperationTypeDto
import java.time.LocalDateTime
import java.util.UUID

data class TransactionInfoState(
    val operation: Operation = Operation(
        id = UUID.randomUUID().toString(),
        senderAccountId = UUID.randomUUID().toString(),
        recipientAccountId = UUID.randomUUID().toString(),
        transactionDateTime = LocalDateTime.now(),
        amount = 1000.0,
        message = "Перевод на карту",
        operationType = OperationTypeDto.TRANSFER,
        directionToMe = false,
    )
)

//val operations = listOf(
//    Operation(
//        id = UUID.randomUUID(),
//        senderAccountId = UUID.randomUUID(),
//        recipientAccountId = UUID.randomUUID(),
//        transactionDateTime = LocalDateTime.of(2025, 2, 25, 14, 30),
//        amount = 5000.0f,
//        message = "Пополнение депозита",
//        operationType = OperationType.REPLENISHMENT
//    ),
//    Operation(
//        id = UUID.randomUUID(),
//        senderAccountId = UUID.randomUUID(),
//        recipientAccountId = UUID.randomUUID(),
//        transactionDateTime = LocalDateTime.of(2025, 2, 24, 10, 0),
//        amount = 200.0f,
//        message = "Снятие наличных в банкомате",
//        operationType = OperationType.WITHDRAWAL
//    ),
//    Operation(
//        id = UUID.randomUUID(),
//        senderAccountId = UUID.randomUUID(),
//        recipientAccountId = UUID.randomUUID(),
//        transactionDateTime = LocalDateTime.of(2025, 2, 23, 16, 45),
//        amount = 1500.0f,
//        message = "Перевод другу",
//        operationType = OperationType.TRANSFER
//    ),
//    Operation(
//        id = UUID.randomUUID(),
//        senderAccountId = UUID.randomUUID(),
//        recipientAccountId = UUID.randomUUID(),
//        transactionDateTime = LocalDateTime.of(2025, 2, 22, 9, 15),
//        amount = 7500.0f,
//        message = "Оплата кредита",
//        operationType = OperationType.LOAN_REPAYMENT
//    )
//)