package com.example.h_bank.presentation.successfulLoanProcessing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulLoanProcessingViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulLoanProcessingNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulLoanProcessingNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulLoanProcessingNavigationEvent.NavigateToMain)
        }
    }
}