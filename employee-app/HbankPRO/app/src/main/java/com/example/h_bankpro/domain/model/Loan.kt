package com.example.h_bankpro.domain.model

import java.time.LocalDate

data class Loan(
    val id: String,
    val documentNumber: Int,
    val amount: Double,
    val debt: Double,
    val ratePercent: Double,
    val isExpired: Boolean,
    val issueDate: LocalDate,
    val endDate: LocalDate
)