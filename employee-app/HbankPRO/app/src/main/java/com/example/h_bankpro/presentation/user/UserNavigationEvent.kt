package com.example.h_bankpro.presentation.user

sealed class UserNavigationEvent {
    data object NavigateToLoan : UserNavigationEvent()
    data class NavigateToAccount(val accountId: String, val accountNumber: String) :
        UserNavigationEvent()

    data object NavigateBack : UserNavigationEvent()
}