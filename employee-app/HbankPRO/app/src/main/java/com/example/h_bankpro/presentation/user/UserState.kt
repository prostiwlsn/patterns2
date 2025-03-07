package com.example.h_bankpro.presentation.user

import com.example.h_bankpro.data.Account
import com.example.h_bankpro.data.Loan
import com.example.h_bankpro.domain.model.User
import java.time.LocalDateTime
import java.util.UUID

data class UserState(
    val user: User? = null,
    val accounts: List<Account> = emptyList(),
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
    val isLoansSheetVisible: Boolean = false,
    val isLoading: Boolean = false
)