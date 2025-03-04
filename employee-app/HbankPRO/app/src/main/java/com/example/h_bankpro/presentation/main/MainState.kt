package com.example.h_bankpro.presentation.main

import com.example.h_bankpro.data.Rate
import com.example.h_bankpro.data.RoleType
import com.example.h_bankpro.data.User
import java.util.UUID

data class MainState(
    val users: List<User> = listOf(
        User(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            phone = "+79991234567",
            name = "Иван Иванов",
            isBlocked = false,
            roles = listOf(RoleType.CLIENT)
        ),
        User(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            phone = "+79997654321",
            name = "Анна Петрова",
            isBlocked = true,
            roles = listOf(RoleType.CLIENT)
        ),
        User(
            id = UUID.fromString("987fcdeb-1234-5678-9abc-def123456789"),
            phone = "+79993332211",
            name = "Пётр Сидоров",
            isBlocked = false,
            roles = listOf(RoleType.CLIENT, RoleType.EMPLOYEE)
        )
    ),

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