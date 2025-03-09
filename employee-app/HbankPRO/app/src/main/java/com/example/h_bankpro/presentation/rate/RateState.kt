package com.example.h_bankpro.presentation.rate

data class RateState(
    val rateId: String = "",
    val interestRate: Double = 0.0,
    val name: String = "",
    val description: String = ""
)