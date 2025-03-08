package com.example.h_bankpro.presentation.rateCreation

data class RateCreationState(
    val interestRate: Double = 0.0,
    val name: String = "",
    val description: String = "",
    val areFieldsValid: Boolean = false,
)