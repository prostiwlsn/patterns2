package com.example.h_bank.presentation.withdrawal

sealed class WithdrawalNavigationEvent {
    data class NavigateToSuccessfulWithdrawal(
        val accountNumber: String,
        val amount: String,
        val currency: String
    ) :
        WithdrawalNavigationEvent()

    data object NavigateBack : WithdrawalNavigationEvent()
}