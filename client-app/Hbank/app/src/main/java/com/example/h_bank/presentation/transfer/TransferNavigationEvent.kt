package com.example.h_bank.presentation.transfer

sealed class TransferNavigationEvent {
    data class NavigateToSuccessfulTransfer(
        val accountNumber: String,
        val beneficiaryAccountNumber: String,
        val amount: String
    ) :
        TransferNavigationEvent()

    data object NavigateBack : TransferNavigationEvent()
}