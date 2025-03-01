package com.example.h_bank.presentation.paymentHistory

sealed class PaymentHistoryNavigationEvent {
    data class NavigateToAccount(val accountId: String) : PaymentHistoryNavigationEvent()
    data class NavigateToPaymentDetails(val paymentId: String) : PaymentHistoryNavigationEvent()
    data object NavigateToTransfer : PaymentHistoryNavigationEvent()
    data object NavigateToHistory : PaymentHistoryNavigationEvent()
    data object NavigateToLoanProcessing : PaymentHistoryNavigationEvent()
    data object NavigateToSuccessfulAccountOpening : PaymentHistoryNavigationEvent()
    data object NavigateBack : PaymentHistoryNavigationEvent()
}