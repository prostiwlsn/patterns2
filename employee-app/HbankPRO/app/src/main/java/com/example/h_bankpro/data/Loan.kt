package com.example.h_bankpro.data

import java.time.LocalDateTime
import java.util.UUID

data class Loan(
    val id: UUID,
    val documentNumber: Int,
    val amount: Int,
    val debt: Int,
    val ratePercent: Double,
    val issueDate: LocalDateTime,
    val endDate: LocalDateTime
)