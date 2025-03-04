package com.example.h_bankpro.presentation.account

sealed class AccountNavigationEvent {
    data class NavigateToTransactionInfo(val transactionId: String) :
        AccountNavigationEvent()

    data object NavigateBack : AccountNavigationEvent()
}