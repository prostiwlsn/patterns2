package com.example.h_bankpro.presentation.account

sealed class AccountNavigationEvent {
    data class NavigateToOperationInfo(val operationId: String) :
        AccountNavigationEvent()

    data object NavigateBack : AccountNavigationEvent()
}