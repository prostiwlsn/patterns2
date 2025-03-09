package com.example.h_bank.domain.entity.loan

data class LoanEntity(
    val amount: Int? = null,
    val duration: Int? = null,
    val rate: Float? = null,
    val dailyPayment: Int? = null,
)