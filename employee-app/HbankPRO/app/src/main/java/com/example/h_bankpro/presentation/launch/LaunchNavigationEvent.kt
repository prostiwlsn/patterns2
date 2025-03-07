package com.example.h_bankpro.presentation.launch

sealed class LaunchNavigationEvent {
    data object NavigateToMain : LaunchNavigationEvent()
    data object NavigateToWelcome : LaunchNavigationEvent()
}