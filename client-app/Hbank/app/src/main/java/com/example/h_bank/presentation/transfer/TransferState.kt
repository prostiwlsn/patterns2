package com.example.h_bank.presentation.transfer

import com.example.h_bank.data.Account

data class TransferState(
    val accounts: List<Account> = emptyList(),
    val amount: String? = null,
    val beneficiaryAccountNumber: String = "",
    val beneficiaryAccountId: String = "",
    val comment: String? = null,
    val selectedAccount: Account? = null,
    val isAccountsSheetVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
    val isLoading: Boolean = false
)