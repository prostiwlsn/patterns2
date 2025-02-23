package com.example.h_bank.presentation.login

sealed class LoginNavigationEvent {
    data object NavigateToWelcome : LoginNavigationEvent()
    data object NavigateToRegister : LoginNavigationEvent()
}