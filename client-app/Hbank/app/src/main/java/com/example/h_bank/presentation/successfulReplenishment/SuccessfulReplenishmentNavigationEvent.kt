package com.example.h_bank.presentation.successfulReplenishment

sealed class SuccessfulReplenishmentNavigationEvent {
    data object NavigateToMain : SuccessfulReplenishmentNavigationEvent()
}