package com.example.h_bank.presentation.withdrawal

sealed class WithdrawalNavigationEvent {
    data class NavigateToSuccessfulWithdrawal(val accountNumber: String, val amount: String) :
        WithdrawalNavigationEvent()

    data object NavigateBack : WithdrawalNavigationEvent()
}