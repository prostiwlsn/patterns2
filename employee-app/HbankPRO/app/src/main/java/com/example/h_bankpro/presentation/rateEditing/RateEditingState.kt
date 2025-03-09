package com.example.h_bankpro.presentation.rateEditing

data class RateEditingState(
    val rateId: String = "",
    val interestRate: String? = null,
    val initialInterestRate: Double = 0.0,
    val name: String = "",
    val initialName: String = "",
    val description: String = "",
    val initialDescription: String = "",
    val areFieldsValid: Boolean = false,
)