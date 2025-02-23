package com.example.h_bankpro.presentation.login

sealed class LoginNavigationEvent {
    data object NavigateToWelcome : LoginNavigationEvent()
    data object NavigateToRegister : LoginNavigationEvent()
}