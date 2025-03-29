package com.example.h_bank.presentation.welcome

sealed class WelcomeNavigationEvent {
    data object NavigateToLogin : WelcomeNavigationEvent()
    data object NavigateToRegister : WelcomeNavigationEvent()
    data object NavigateToMain : WelcomeNavigationEvent()
}