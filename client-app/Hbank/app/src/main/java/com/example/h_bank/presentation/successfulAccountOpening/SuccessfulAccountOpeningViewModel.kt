package com.example.h_bank.presentation.successfulAccountOpening

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.presentation.login.LoginNavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulAccountOpeningViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulAccountOpeningNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulAccountOpeningNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulAccountOpeningNavigationEvent.NavigateToMain)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulAccountOpeningNavigationEvent.NavigateBack)
        }
    }
}