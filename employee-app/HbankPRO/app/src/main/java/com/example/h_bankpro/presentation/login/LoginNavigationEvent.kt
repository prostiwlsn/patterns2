package com.example.h_bankpro.presentation.login

sealed class LoginNavigationEvent {
    data object NavigateToRegister : LoginNavigationEvent()
    data object NavigateToMain : LoginNavigationEvent()
    data object NavigateBack : LoginNavigationEvent()
}