package com.example.h_bank.presentation.transfer

sealed class TransferNavigationEvent {
    data class NavigateToSuccessfulTransfer(
        val accountId: String,
        val beneficiaryAccountId: String,
        val amount: Long
    ) :
        TransferNavigationEvent()

    data object NavigateBack : TransferNavigationEvent()
}