package com.example.h_bank.presentation.replenishment

sealed class ReplenishmentNavigationEvent {
    data class NavigateToSuccessfulReplenishment(
        val accountNumber: String,
        val amount: String,
        val currency: String
    ) :
        ReplenishmentNavigationEvent()

    data object NavigateBack : ReplenishmentNavigationEvent()
}