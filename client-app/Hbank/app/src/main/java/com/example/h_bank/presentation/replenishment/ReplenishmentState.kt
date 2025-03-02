package com.example.h_bank.presentation.replenishment

import com.example.h_bank.data.Account

data class ReplenishmentState(
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
    val amount: Long = 0,
    val selectedAccount: Account = Account("1", "Счёт 1", 100000),
    val isAccountsSheetVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
)