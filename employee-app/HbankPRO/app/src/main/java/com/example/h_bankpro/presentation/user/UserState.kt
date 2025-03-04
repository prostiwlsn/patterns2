package com.example.h_bankpro.presentation.user

import com.example.h_bankpro.data.Account
import com.example.h_bankpro.data.Loan
import com.example.h_bankpro.data.RoleType
import com.example.h_bankpro.data.User
import java.time.LocalDateTime
import java.util.UUID

data class UserState(
    val user: User = User(
        id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        phone = "+79991234567",
        name = "Иван Иванов",
        isBlocked = false,
        roles = listOf(RoleType.CLIENT)
    ),
    val accounts: List<Account> = listOf(
        Account("1", "Счёт 1", 100000),
        Account("1", "Счёт 1", 100000),
        Account("1", "Счёт 1", 100000),
        Account("1", "Счёт 1", 100000),
        Account("1", "Счёт 1", 100000),
        Account("1", "Счёт 1", 100000),
        Account("1", "Счёт 1", 100000),
        Account("1", "Счёт 1", 100000),
        Account("1", "Счёт 1", 100000),
    ),
    val loans: List<Loan> = listOf(
        Loan(
            id = UUID.randomUUID(),
            documentNumber = 123456,
            amount = 500_000,
            debt = 250_000,
            ratePercent = 12.5,
            issueDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        ),
        Loan(
            id = UUID.randomUUID(),
            documentNumber = 123456,
            amount = 500_000,
            debt = 250_000,
            ratePercent = 12.5,
            issueDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        ),
        Loan(
            id = UUID.randomUUID(),
            documentNumber = 123456,
            amount = 500_000,
            debt = 250_000,
            ratePercent = 12.5,
            issueDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        ),
        Loan(
            id = UUID.randomUUID(),
            documentNumber = 123456,
            amount = 500_000,
            debt = 250_000,
            ratePercent = 12.5,
            issueDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        ),
        Loan(
            id = UUID.randomUUID(),
            documentNumber = 123456,
            amount = 500_000,
            debt = 250_000,
            ratePercent = 12.5,
            issueDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        ),
        Loan(
            id = UUID.randomUUID(),
            documentNumber = 123456,
            amount = 500_000,
            debt = 250_000,
            ratePercent = 12.5,
            issueDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        ),
    ),
    val isAccountsSheetVisible: Boolean = false,
    val isLoansSheetVisible: Boolean = false
)