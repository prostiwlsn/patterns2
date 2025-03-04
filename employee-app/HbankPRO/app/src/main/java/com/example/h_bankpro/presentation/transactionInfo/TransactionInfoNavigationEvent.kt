package com.example.h_bankpro.presentation.transactionInfo

sealed class TransactionInfoNavigationEvent {
    data object NavigateBack : TransactionInfoNavigationEvent()
}