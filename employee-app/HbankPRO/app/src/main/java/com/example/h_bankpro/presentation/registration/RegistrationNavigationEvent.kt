package com.example.h_bankpro.presentation.registration

sealed class RegistrationNavigationEvent {
    data object NavigateToLogin : RegistrationNavigationEvent()
    data object NavigateToMain : RegistrationNavigationEvent()
    data object NavigateBack : RegistrationNavigationEvent()
}