package com.example.h_bankpro.presentation.rateCreation

sealed class RateCreationNavigationEvent {
    data object NavigateToSuccessfulRateCreation : RateCreationNavigationEvent()
    data object NavigateBack : RateCreationNavigationEvent()
}