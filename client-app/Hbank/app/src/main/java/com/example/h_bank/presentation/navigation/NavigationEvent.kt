package com.example.h_bank.presentation.navigation

import com.example.h_bank.presentation.main.MainNavigationEvent

sealed interface NavigationEvent {
    data object NavigateToNoConnection : NavigationEvent
}