package com.example.h_bank.presentation.loan

sealed class LoanNavigationEvent {
    data class NavigateToLoanPayment(
        val loanId: String,
        val documentNumber: String,
        val debt: String
    ) : LoanNavigationEvent()

    data object NavigateBack : LoanNavigationEvent()
}