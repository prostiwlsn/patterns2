package com.example.h_bankpro.presentation.user

sealed class UserNavigationEvent {
    data object NavigateToLoan : UserNavigationEvent()
    data object NavigateToAccount : UserNavigationEvent()
    data object NavigateBack : UserNavigationEvent()
}