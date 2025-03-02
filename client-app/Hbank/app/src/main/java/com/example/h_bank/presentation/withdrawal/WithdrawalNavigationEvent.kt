package com.example.h_bank.presentation.withdrawal

sealed class WithdrawalNavigationEvent {
    data class NavigateToSuccessfulWithdrawal(val accountId: String, val amount: Long) :
        WithdrawalNavigationEvent()

    data object NavigateBack : WithdrawalNavigationEvent()
}