package com.example.h_bank.presentation.successfulReplenishment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuccessfulReplenishmentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val amount: Double = checkNotNull(savedStateHandle["amount"])
    val accountId: String = checkNotNull(savedStateHandle["accountId"])

    private val _state = MutableStateFlow(SuccessfulReplenishmentState())
    val state: StateFlow<SuccessfulReplenishmentState> = _state

    private val _navigationEvent = MutableSharedFlow<SuccessfulReplenishmentNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulReplenishmentNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulReplenishmentNavigationEvent.NavigateToMain)
        }
    }
}