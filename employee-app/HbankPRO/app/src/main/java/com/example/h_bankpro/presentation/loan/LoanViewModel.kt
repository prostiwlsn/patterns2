package com.example.h_bankpro.presentation.loan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoanViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoanState())
    val state: StateFlow<LoanState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanNavigationEvent>()
    val navigationEvent: SharedFlow<LoanNavigationEvent> = _navigationEvent

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanNavigationEvent.NavigateBack)
        }
    }
}