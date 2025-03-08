package com.example.h_bankpro.presentation.loan

import com.example.h_bankpro.domain.model.Loan
import java.time.LocalDateTime
import java.util.UUID

data class LoanState(
    val loan: Loan = Loan(
        id = "",
        amount = 500.0,
        userId = "",
        tariffId = "",
        status = "",
    ),
)