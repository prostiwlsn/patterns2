package com.example.h_bank.presentation.successfulLoanProcessing

sealed class SuccessfulLoanProcessingNavigationEvent {
    data object NavigateToMain : SuccessfulLoanProcessingNavigationEvent()
    data object NavigateBack : SuccessfulLoanProcessingNavigationEvent()
}