package com.example.h_bankpro.presentation.main

sealed class MainNavigationEvent {
    data class NavigateToUser(val userId: String) : MainNavigationEvent()
    data object NavigateToRate : MainNavigationEvent()
    data object NavigateToRateCreation : MainNavigationEvent()
    data object NavigateToUserCreation : MainNavigationEvent()
    data object NavigateToWelcome : MainNavigationEvent()
}