package com.example.h_bank.presentation.successfulLoanPayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulLoanPaymentViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulLoanPaymentNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulLoanPaymentNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulLoanPaymentNavigationEvent.NavigateToMain)
        }
    }
}