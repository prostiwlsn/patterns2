package com.example.h_bank.presentation.successfulTransfer

import com.example.h_bank.data.Account
import com.example.h_bank.data.Payment
import com.example.h_bank.data.PaymentType
import java.time.LocalDate
import java.time.LocalDateTime

data class SuccessfulTransferState(
    val payment: Payment = Payment(
        "1",
        PaymentType.OUTGOING,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    )
)