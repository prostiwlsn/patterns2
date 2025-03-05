package com.example.h_bankpro.presentation.main

import com.example.h_bankpro.data.Rate
import com.example.h_bankpro.domain.model.User
import java.util.UUID

data class MainState(
    val users: List<User> = emptyList(),
    val rates: List<Rate> = listOf(
        Rate(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Ставка А",
            interestRate = 5.75f,
            description = "Базовая ставка для новых клиентов"
        ),
        Rate(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Ставка А",
            interestRate = 5.75f,
            description = "Базовая ставка для новых клиентов"
        ),
        Rate(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Ставка А",
            interestRate = 5.75f,
            description = "Базовая ставка для новых клиентов"
        ),
        Rate(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            name = "Ставка А",
            interestRate = 5.75f,
            description = "Базовая ставка для новых клиентов"
        ),
    ),
    val isUsersSheetVisible: Boolean = false,
    val isRatesSheetVisible: Boolean = false
)