package com.example.h_bank.presentation.loan

import com.example.h_bank.data.Loan
import java.time.LocalDateTime
import java.util.UUID

data class LoanState(
    val loan: Loan = Loan(
        id = UUID.randomUUID(),
        documentNumber = 123456,
        amount = 500_000,
        debt = 250_000,
        ratePercent = 12.5,
        issueDate = LocalDateTime.now(),
        endDate = LocalDateTime.now()
    ),
)