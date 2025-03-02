package com.example.h_bank.presentation.successfulTransfer

sealed class SuccessfulTransferNavigationEvent {
    data object NavigateToMain : SuccessfulTransferNavigationEvent()
}