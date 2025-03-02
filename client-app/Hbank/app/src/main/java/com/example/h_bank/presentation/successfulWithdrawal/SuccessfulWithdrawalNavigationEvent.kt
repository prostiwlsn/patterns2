package com.example.h_bank.presentation.successfulWithdrawal

sealed class SuccessfulWithdrawalNavigationEvent {
    data object NavigateToMain : SuccessfulWithdrawalNavigationEvent()
}