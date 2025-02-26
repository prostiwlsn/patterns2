package com.example.h_bank.presentation.loanProcessing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Rate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanProcessingViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoanProcessingState())
    val state: StateFlow<LoanProcessingState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanProcessingNavigationEvent>()
    val navigationEvent: SharedFlow<LoanProcessingNavigationEvent> = _navigationEvent

    fun onAmountChange(amount: Int) {
        _state.update { it.copy(amount = amount) }
        validateFields()
    }

    fun onTermChange(term: Int) {
        _state.update { it.copy(term = term) }
        validateFields()
    }

    fun onRateClicked(selectedRate: Rate) {
        _state.update { it.copy(selectedRate = selectedRate) }
    }

    fun onProcessLoanClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanProcessingNavigationEvent.NavigateToSuccessfulLoanProcessing)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanProcessingNavigationEvent.NavigateBack)
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.amount != 0 &&
                        _state.value.term != 0
            )
        }
    }

    fun showRatesSheet() {
        _state.update { it.copy(isRatesSheetVisible = true) }
    }

    fun hideRatesSheet() {
        _state.update { it.copy(isRatesSheetVisible = false) }
    }
}