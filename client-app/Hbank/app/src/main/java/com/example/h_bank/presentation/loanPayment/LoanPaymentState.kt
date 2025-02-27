package com.example.h_bank.presentation.loanPayment

import com.example.h_bank.data.Account

data class LoanPaymentState(
    val accounts: List<Account> = listOf(
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
    ),
    val amount: Int = 0,
    val selectedAccount: Account = Account("1", "Счёт 1", "100000"),
    val isAccountsSheetVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
)