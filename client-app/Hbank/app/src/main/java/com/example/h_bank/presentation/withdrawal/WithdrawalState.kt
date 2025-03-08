package com.example.h_bank.presentation.withdrawal

import com.example.h_bank.data.Account
import java.time.LocalDateTime

data class WithdrawalState(
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
    val amount: Long = 0,
    val selectedAccount: Account = Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
    val isAccountsSheetVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
)