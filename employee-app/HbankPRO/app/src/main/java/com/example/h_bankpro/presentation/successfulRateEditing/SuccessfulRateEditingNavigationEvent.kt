package com.example.h_bankpro.presentation.successfulRateEditing

sealed class SuccessfulRateEditingNavigationEvent {
    data object NavigateToMain : SuccessfulRateEditingNavigationEvent()
}