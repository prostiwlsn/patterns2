package com.example.h_bankpro.presentation.successfulUserCreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulUserCreationViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulUserCreationNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulUserCreationNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulUserCreationNavigationEvent.NavigateToMain)
        }
    }
}