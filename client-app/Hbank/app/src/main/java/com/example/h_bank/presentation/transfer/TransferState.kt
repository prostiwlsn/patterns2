package com.example.h_bank.presentation.transfer

import com.example.h_bank.data.Account

data class TransferState(
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
    val beneficiaryAccountId: String = "",
    val comment: String = "",
    val selectedAccount: Account = Account("1", "Счёт 1", 100000),
    val isAccountsSheetVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
)