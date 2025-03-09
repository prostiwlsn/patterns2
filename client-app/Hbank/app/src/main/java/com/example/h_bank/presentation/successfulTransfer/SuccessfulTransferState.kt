package com.example.h_bank.presentation.successfulTransfer

data class SuccessfulTransferState(
    val accountNumber: String = "",
    val beneficiaryAccountNumber: String = "",
    val amount: String = ""
)