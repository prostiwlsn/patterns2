package com.example.h_bank.data

import java.time.LocalDate

data class Payment(
    val id: String,
    val type: PaymentType,
    val date: LocalDate,
    val amount: Double,
    val account: Account
)