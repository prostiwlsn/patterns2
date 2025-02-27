package com.example.h_bank.presentation.loanPayment

sealed class LoanPaymentNavigationEvent {
    data object NavigateToSuccessfulLoanPayment : LoanPaymentNavigationEvent()
    data object NavigateBack : LoanPaymentNavigationEvent()
}