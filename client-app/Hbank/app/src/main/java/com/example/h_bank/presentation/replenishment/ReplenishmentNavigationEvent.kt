package com.example.h_bank.presentation.replenishment

sealed class ReplenishmentNavigationEvent {
    data class NavigateToSuccessfulReplenishment(val accountId: String, val amount: Long) :
        ReplenishmentNavigationEvent()

    data object NavigateBack : ReplenishmentNavigationEvent()
}