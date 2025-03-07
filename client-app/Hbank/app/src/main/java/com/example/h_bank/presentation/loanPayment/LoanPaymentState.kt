package com.example.h_bank.presentation.loanPayment

import com.example.h_bank.data.Account

data class LoanPaymentState(
    val accounts: List<Account> = listOf(),
    val amount: Int = 0,
    val selectedAccount: Account? = null,
    val isAccountsSheetVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
)