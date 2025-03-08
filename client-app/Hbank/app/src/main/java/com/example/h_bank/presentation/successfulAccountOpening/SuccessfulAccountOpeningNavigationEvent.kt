package com.example.h_bank.presentation.successfulAccountOpening


sealed class SuccessfulAccountOpeningNavigationEvent {
    data object NavigateToMain : SuccessfulAccountOpeningNavigationEvent()
}