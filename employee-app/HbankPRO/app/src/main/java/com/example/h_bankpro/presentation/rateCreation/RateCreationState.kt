package com.example.h_bankpro.presentation.rateCreation

data class RateCreationState(
    val interestRate: String = "",
    val name: String = "",
    val description: String = "",
    val areFieldsValid: Boolean = false,
)