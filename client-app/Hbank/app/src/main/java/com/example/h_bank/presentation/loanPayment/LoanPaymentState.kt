package com.example.h_bank.presentation.loanPayment

import com.example.h_bank.data.Account

data class LoanPaymentState(
    val currentUserId: String = "",
    val accounts: List<Account> = emptyList(),
    val amount: Double? = null,
    val selectedAccount: Account? = null,
    val isAccountsSheetVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
    val isLoading: Boolean = false,
)