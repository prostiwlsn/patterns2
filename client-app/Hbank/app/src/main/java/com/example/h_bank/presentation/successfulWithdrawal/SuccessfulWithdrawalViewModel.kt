package com.example.h_bank.presentation.successfulWithdrawal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuccessfulWithdrawalViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val amount: Double = checkNotNull(savedStateHandle["amount"])
    val accountId: String = checkNotNull(savedStateHandle["accountId"])

    private val _state = MutableStateFlow(SuccessfulWithdrawalState())
    val state: StateFlow<SuccessfulWithdrawalState> = _state

    private val _navigationEvent = MutableSharedFlow<SuccessfulWithdrawalNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulWithdrawalNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulWithdrawalNavigationEvent.NavigateToMain)
        }
    }
}