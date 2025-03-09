package com.example.h_bank.presentation.successfulLoanPayment

sealed class SuccessfulLoanPaymentNavigationEvent {
    data object NavigateToMain : SuccessfulLoanPaymentNavigationEvent()
}