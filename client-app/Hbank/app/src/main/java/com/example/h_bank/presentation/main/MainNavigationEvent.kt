package com.example.h_bank.presentation.main

sealed class MainNavigationEvent {
    data class NavigateToAccount(val accountId: String) : MainNavigationEvent()
    data class NavigateToLoan(val loanId: String) : MainNavigationEvent()
    data object NavigateToTransfer : MainNavigationEvent()
    data object NavigateToPaymentHistory : MainNavigationEvent()
    data object NavigateToLoanProcessing : MainNavigationEvent()
    data object NavigateToSuccessfulAccountOpening : MainNavigationEvent()
}