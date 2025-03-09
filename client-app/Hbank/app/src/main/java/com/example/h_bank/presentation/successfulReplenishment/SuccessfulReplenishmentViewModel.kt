package com.example.h_bank.presentation.successfulReplenishment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SuccessfulReplenishmentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _state = MutableStateFlow(SuccessfulReplenishmentState())
    val state: StateFlow<SuccessfulReplenishmentState> = _state

    private val _navigationEvent = MutableSharedFlow<SuccessfulReplenishmentNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulReplenishmentNavigationEvent> = _navigationEvent

    private val amount: String = checkNotNull(savedStateHandle["amount"])
    private val accountNumber: String = checkNotNull(savedStateHandle["accountNumber"])

    init {
        _state.update { it.copy(accountNumber = accountNumber, amount = amount) }
    }

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulReplenishmentNavigationEvent.NavigateToMain)
        }
    }
}