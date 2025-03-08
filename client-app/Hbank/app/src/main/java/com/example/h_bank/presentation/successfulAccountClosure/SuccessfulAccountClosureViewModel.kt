package com.example.h_bank.presentation.successfulAccountClosure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulAccountClosureViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulAccountClosureNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulAccountClosureNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulAccountClosureNavigationEvent.NavigateToMain)
        }
    }
}