package com.example.h_bank.presentation.launch

sealed class LaunchNavigationEvent {
    data object NavigateToMain : LaunchNavigationEvent()
    data object NavigateToWelcome : LaunchNavigationEvent()
}