package com.example.h_bank.presentation.connectionErrorScreen

sealed class ConnectionErrorNavigationEvent {
    data object NavigateBack : ConnectionErrorNavigationEvent()
}