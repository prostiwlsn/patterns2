package com.example.h_bankpro.presentation.successfulRateDeletion

sealed class SuccessfulRateDeletionNavigationEvent {
    data object NavigateToMain : SuccessfulRateDeletionNavigationEvent()
}