package com.example.h_bankpro.presentation.successfulRateEditing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulRateEditingViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulRateEditingNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulRateEditingNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulRateEditingNavigationEvent.NavigateToMain)
        }
    }
}