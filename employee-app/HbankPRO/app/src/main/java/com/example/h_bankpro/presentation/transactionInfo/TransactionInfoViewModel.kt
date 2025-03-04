package com.example.h_bankpro.presentation.transactionInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionInfoViewModel : ViewModel() {
    private val _state = MutableStateFlow(TransactionInfoState())
    val state: StateFlow<TransactionInfoState> = _state

    private val _navigationEvent = MutableSharedFlow<TransactionInfoNavigationEvent>()
    val navigationEvent: SharedFlow<TransactionInfoNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(TransactionInfoNavigationEvent.NavigateBack)
        }
    }
}