package com.example.h_bank.presentation.connectionErrorScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ConnectionErrorViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<ConnectionErrorNavigationEvent>()
    val navigationEvent: SharedFlow<ConnectionErrorNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(ConnectionErrorNavigationEvent.NavigateBack)
        }
    }
}