package com.example.h_bank.presentation.successfulAccountClosure

sealed class SuccessfulAccountClosureNavigationEvent {
    data object NavigateToMain : SuccessfulAccountClosureNavigationEvent()
}