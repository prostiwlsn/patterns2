package com.example.h_bank.presentation.successfulTransfer

import com.example.h_bank.data.Account
import com.example.h_bank.domain.entity.payment.PaymentItemEntity
import com.example.h_bank.domain.entity.filter.OperationType
import java.time.LocalDate
import java.time.LocalDateTime

data class SuccessfulTransferState(
    val payment: PaymentItemEntity = PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    )
)