package com.example.h_bank.presentation.main

import com.example.h_bank.data.Account
import com.example.h_bank.data.Loan

data class MainState(
    val accounts: List<Account> = listOf(
        Account("1", "Счёт 1", "24 420,05 ₽"),
        Account("2", "Счёт 2", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
        Account("3", "Счёт 3", "10 000,00 ₽"),
    ),
    val loans: List<Loan> = listOf(
        Loan(
            "1", "Кредит 1", "5 000,00 ₽",
            dueDate = "25.05.2026"
        ),
        Loan("2", "Кредит 2", "7 500,00 ₽",
            dueDate = "25.05.2026"
        )
    ),
    val isAccountsSheetVisible: Boolean = false,
    val isLoansSheetVisible: Boolean = false
)