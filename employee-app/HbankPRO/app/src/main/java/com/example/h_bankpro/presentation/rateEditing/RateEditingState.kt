package com.example.h_bankpro.presentation.rateEditing

import com.example.h_bankpro.data.Rate
import java.util.UUID

data class RateEditingState(
    val interestRate: Float = 0.0f,
    val name: String = "",
    val description: String = "",
    val rate: Rate = Rate(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        name = "Ставка А",
        interestRate = 5.75f,
        description = "Базовая ставка для новых клиентов"
    ),
    val areFieldsValid: Boolean = false,
)