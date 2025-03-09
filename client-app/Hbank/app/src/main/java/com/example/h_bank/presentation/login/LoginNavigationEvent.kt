package com.example.h_bank.presentation.login

sealed class LoginNavigationEvent {
    data object NavigateToRegister : LoginNavigationEvent()
    data object NavigateToMain : LoginNavigationEvent()
    data object NavigateBack : LoginNavigationEvent()
}