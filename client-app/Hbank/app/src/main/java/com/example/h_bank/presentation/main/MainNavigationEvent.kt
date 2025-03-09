package com.example.h_bank.presentation.main

sealed class MainNavigationEvent {
    data class NavigateToAccount(val accountId: String) : MainNavigationEvent()
    data class NavigateToLoan(
        val loanId: String,
        val documentNumber: String,
        val amount: String,
        val endDate: String,
        val ratePercent: String,
        val debt: String
    ) : MainNavigationEvent()

    data class NavigateToTransfer(val userId: String) : MainNavigationEvent()
    data object NavigateToPaymentHistory : MainNavigationEvent()
    data object NavigateToLoanProcessing : MainNavigationEvent()
    data class NavigateToWithdrawal(val userId: String) : MainNavigationEvent()
    data class NavigateToReplenishment(val userId: String) : MainNavigationEvent()
    data object NavigateToSuccessfulAccountOpening : MainNavigationEvent()
    data object NavigateToSuccessfulAccountClosure : MainNavigationEvent()
    data object NavigateToWelcome : MainNavigationEvent()
}