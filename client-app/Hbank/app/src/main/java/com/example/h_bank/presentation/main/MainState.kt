package com.example.h_bank.presentation.main

import com.example.h_bank.data.Account
import com.example.h_bank.data.Loan
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class MainState(
    val accounts: List<Account> = listOf(
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
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