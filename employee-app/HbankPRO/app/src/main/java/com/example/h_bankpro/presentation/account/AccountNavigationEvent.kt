package com.example.h_bankpro.presentation.account

sealed class AccountNavigationEvent {
    data class NavigateToOperationInfo(val accountId: String, val operationId: String) :
        AccountNavigationEvent()

    data object NavigateBack : AccountNavigationEvent()
}