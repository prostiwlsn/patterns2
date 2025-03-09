package com.example.h_bankpro.presentation.successfulUserCreation

sealed class SuccessfulUserCreationNavigationEvent {
    data object NavigateToMain : SuccessfulUserCreationNavigationEvent()
}