package com.example.h_bankpro.presentation.rate

sealed class RateNavigationEvent {
    data class NavigateToRateEditing(
        val rateId: String,
        val name: String,
        val interestRate: String,
        val description: String
    ) : RateNavigationEvent()

    data object NavigateBack : RateNavigationEvent()
    data object NavigateToSuccessfulRateDeletion : RateNavigationEvent()
}