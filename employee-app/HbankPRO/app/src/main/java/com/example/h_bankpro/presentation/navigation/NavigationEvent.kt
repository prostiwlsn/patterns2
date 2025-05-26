package com.example.h_bankpro.presentation.navigation

sealed interface NavigationEvent {
    data object NavigateToNoConnection : NavigationEvent
    data object NavigateToServerError : NavigationEvent
}