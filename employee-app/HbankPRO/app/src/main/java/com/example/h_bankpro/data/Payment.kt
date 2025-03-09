package com.example.h_bankpro.data

import java.time.LocalDate

data class Payment(
    val id: String,
    val type: PaymentType,
    val date: LocalDate,
    val amount: Double,
    val account: Account,
    val comment: String?
)