package com.example.h_bankpro.presentation.operationInfo

sealed class OperationInfoNavigationEvent {
    data object NavigateBack : OperationInfoNavigationEvent()
}