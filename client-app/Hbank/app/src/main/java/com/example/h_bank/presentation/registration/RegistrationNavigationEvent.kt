package com.example.h_bank.presentation.registration

sealed class RegistrationNavigationEvent {
    data object NavigateToWelcome : RegistrationNavigationEvent()
    data object NavigateToLogin : RegistrationNavigationEvent()
}