package com.example.h_bank.presentation.loanProcessing

sealed class LoanProcessingNavigationEvent {
    data object NavigateToSuccessfulLoanProcessing : LoanProcessingNavigationEvent()
    data object NavigateBack : LoanProcessingNavigationEvent()
}