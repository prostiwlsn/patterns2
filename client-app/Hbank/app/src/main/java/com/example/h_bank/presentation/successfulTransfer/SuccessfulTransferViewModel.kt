package com.example.h_bank.presentation.successfulTransfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuccessfulTransferViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val accountId: String = checkNotNull(savedStateHandle["accountId"])
    val beneficiaryAccountId: String = checkNotNull(savedStateHandle["beneficiaryAccountId"])
    val amount: Long = checkNotNull(savedStateHandle["amount"])

    private val _state = MutableStateFlow(SuccessfulTransferState())
    val state: StateFlow<SuccessfulTransferState> = _state

    private val _navigationEvent = MutableSharedFlow<SuccessfulTransferNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulTransferNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulTransferNavigationEvent.NavigateToMain)
        }
    }


}