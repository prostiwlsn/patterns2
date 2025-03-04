package com.example.h_bankpro.presentation.rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RateViewModel : ViewModel() {
    private val _state = MutableStateFlow(RateState())
    val state: StateFlow<RateState> = _state

    private val _navigationEvent = MutableSharedFlow<RateNavigationEvent>()
    val navigationEvent: SharedFlow<RateNavigationEvent> = _navigationEvent

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(RateNavigationEvent.NavigateBack)
        }
    }

    fun onDeleteClicked() {
    }

    fun onEditClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                RateNavigationEvent.NavigateToRateEditing
            )
        }
    }
}