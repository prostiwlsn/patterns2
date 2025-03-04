package com.example.h_bankpro.presentation.rate

sealed class RateNavigationEvent {
    data object NavigateToRateEditing : RateNavigationEvent()
    data object NavigateBack : RateNavigationEvent()
}