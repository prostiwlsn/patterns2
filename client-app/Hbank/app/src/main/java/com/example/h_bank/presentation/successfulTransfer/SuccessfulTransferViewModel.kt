package com.example.h_bank.presentation.successfulTransfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SuccessfulTransferViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _state = MutableStateFlow(SuccessfulTransferState())
    val state: StateFlow<SuccessfulTransferState> = _state

    private val _navigationEvent = MutableSharedFlow<SuccessfulTransferNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulTransferNavigationEvent> = _navigationEvent

    private val accountNumber: String = checkNotNull(savedStateHandle["accountNumber"])
    private val beneficiaryAccountNumber: String =
        checkNotNull(savedStateHandle["beneficiaryAccountNumber"])
    private val amount: String = checkNotNull(savedStateHandle["amount"])

    init {
        _state.update {
            it.copy(
                accountNumber = accountNumber,
                beneficiaryAccountNumber = beneficiaryAccountNumber,
                amount = amount
            )
        }
    }

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulTransferNavigationEvent.NavigateToMain)
        }
    }


}