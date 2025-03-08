package com.example.h_bank.data

import java.time.LocalDate

data class Loan(
    val id: String,
    val documentNumber: Int,
    val amount: Double,
    val debt: Double,
    val ratePercent: Double,
    val issueDate: LocalDate,
    val endDate: LocalDate
)