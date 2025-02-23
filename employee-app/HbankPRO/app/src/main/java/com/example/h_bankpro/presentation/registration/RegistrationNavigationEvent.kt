package com.example.h_bankpro.presentation.registration

sealed class RegistrationNavigationEvent {
    data object NavigateToWelcome : RegistrationNavigationEvent()
    data object NavigateToLogin : RegistrationNavigationEvent()
}