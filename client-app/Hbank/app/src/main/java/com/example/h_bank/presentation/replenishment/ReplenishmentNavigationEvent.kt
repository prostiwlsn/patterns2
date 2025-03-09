package com.example.h_bank.presentation.replenishment

sealed class ReplenishmentNavigationEvent {
    data class NavigateToSuccessfulReplenishment(val accountNumber: String, val amount: String) :
        ReplenishmentNavigationEvent()

    data object NavigateBack : ReplenishmentNavigationEvent()
}