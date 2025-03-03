package com.example.h_bankpro.presentation.rateCreation

data class RateCreationState(
    val interestRate: Float = 0.0f,
    val name: String = "",
    val description: String = "",
    val areFieldsValid: Boolean = false,
)