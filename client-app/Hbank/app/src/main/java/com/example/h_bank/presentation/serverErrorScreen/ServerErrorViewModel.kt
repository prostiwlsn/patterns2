package com.example.h_bank.presentation.serverErrorScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ServerErrorViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<ServerErrorNavigationEvent>()
    val navigationEvent: SharedFlow<ServerErrorNavigationEvent> = _navigationEvent

    fun onRetryClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(ServerErrorNavigationEvent.NavigateBack)
        }
    }
}

sealed interface ServerErrorNavigationEvent {
    data object NavigateBack : ServerErrorNavigationEvent
}