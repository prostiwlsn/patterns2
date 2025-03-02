package com.example.h_bank.presentation.successfulTransfer

import com.example.h_bank.data.Account
import com.example.h_bank.data.Payment
import com.example.h_bank.data.PaymentType
import java.time.LocalDate

data class SuccessfulTransferState(
    val payment: Payment = Payment(
        "1",
        PaymentType.OUTGOING,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("2", "Счёт 2", 100000),
        null
    )
)