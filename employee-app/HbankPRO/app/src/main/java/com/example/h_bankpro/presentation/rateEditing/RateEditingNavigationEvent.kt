package com.example.h_bankpro.presentation.rateEditing

sealed class RateEditingNavigationEvent {
    data object NavigateToSuccessfulRateEditing : RateEditingNavigationEvent()
    data object NavigateBack : RateEditingNavigationEvent()
}