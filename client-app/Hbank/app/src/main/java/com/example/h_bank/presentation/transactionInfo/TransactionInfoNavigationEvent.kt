package com.example.h_bank.presentation.transactionInfo

sealed class TransactionInfoNavigationEvent {
    data object NavigateBack : TransactionInfoNavigationEvent()
}