package com.example.h_bank.presentation.loan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _state = MutableStateFlow(LoanState())
    val state: StateFlow<LoanState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanNavigationEvent>()
    val navigationEvent: SharedFlow<LoanNavigationEvent> = _navigationEvent

    private val loanId: String = checkNotNull(savedStateHandle["loanId"])
    private val documentNumber: String = checkNotNull(savedStateHandle["documentNumber"])
    private val amount: String = checkNotNull(savedStateHandle["amount"])
    private val endDate: String = checkNotNull(savedStateHandle["endDate"])
    private val ratePercent: String = checkNotNull(savedStateHandle["ratePercent"])
    private val debt: String = checkNotNull(savedStateHandle["debt"])

    init {
        _state.update {
            it.copy(
                documentNumber = documentNumber,
                amount = amount,
                endDate = endDate,
                ratePercent = ratePercent,
                debt = debt,
            )
        }
    }

    fun onPayClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanNavigationEvent.NavigateToLoanPayment(loanId))
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanNavigationEvent.NavigateBack)
        }
    }
}