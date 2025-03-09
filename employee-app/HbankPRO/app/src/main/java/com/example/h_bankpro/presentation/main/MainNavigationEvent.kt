package com.example.h_bankpro.presentation.main

sealed class MainNavigationEvent {
    data class NavigateToUser(val userId: String) : MainNavigationEvent()
    data class NavigateToRate(
        val rateId: String,
        val name: String,
        val interestRate: String,
        val description: String
    ) :
        MainNavigationEvent()

    data object NavigateToRateCreation : MainNavigationEvent()
    data object NavigateToUserCreation : MainNavigationEvent()
    data object NavigateToWelcome : MainNavigationEvent()
}