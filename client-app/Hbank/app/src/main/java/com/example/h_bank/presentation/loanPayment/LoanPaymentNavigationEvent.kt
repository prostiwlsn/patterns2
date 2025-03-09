package com.example.h_bank.presentation.loanPayment

sealed class LoanPaymentNavigationEvent {
    data class NavigateToSuccessfulLoanPayment(
        val documentNumber: String,
        val amount: String,
        val debt: String,
        val accountNumber: String
    ) : LoanPaymentNavigationEvent()

    data object NavigateBack : LoanPaymentNavigationEvent()
}