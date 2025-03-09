package com.example.h_bank.presentation.navigation

sealed interface NavigationEvent {
    data object NavigateToNoConnection : NavigationEvent
}