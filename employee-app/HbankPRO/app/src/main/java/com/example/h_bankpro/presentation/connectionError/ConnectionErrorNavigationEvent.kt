package com.example.h_bankpro.presentation.connectionError

sealed class ConnectionErrorNavigationEvent {
    data object NavigateBack : ConnectionErrorNavigationEvent()
}