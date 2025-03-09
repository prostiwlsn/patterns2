package com.example.h_bank.presentation.replenishment

import com.example.h_bank.data.Account

data class ReplenishmentState(
    val accounts: List<Account> = emptyList(),
    val amount: String? = null,
    val selectedAccount: Account? = null,
    val isAccountsSheetVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
    val isLoading: Boolean = false
)