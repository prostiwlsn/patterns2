package com.example.h_bankpro.presentation.user

sealed class UserNavigationEvent {
    data class NavigateToLoan(
        val documentNumber: String,
        val amount: String,
        val endDate: String,
        val ratePercent: String,
        val debt: String
    ) : UserNavigationEvent()

    data class NavigateToAccount(val accountId: String, val accountNumber: String) :
        UserNavigationEvent()

    data object NavigateBack : UserNavigationEvent()
}