package com.example.h_bankpro.presentation.successfulRateDeletion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulRateDeletionViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulRateDeletionNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulRateDeletionNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulRateDeletionNavigationEvent.NavigateToMain)
        }
    }
}