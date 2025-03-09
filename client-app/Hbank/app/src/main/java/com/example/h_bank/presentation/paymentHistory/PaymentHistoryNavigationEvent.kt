package com.example.h_bank.presentation.paymentHistory

sealed class PaymentHistoryNavigationEvent {
    data class NavigateToTransactionInfo(val transactionId: String) :
        PaymentHistoryNavigationEvent()

    data object NavigateBack : PaymentHistoryNavigationEvent()
}