package com.example.h_bank.presentation.loanPayment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanPaymentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val loanId: String = checkNotNull(savedStateHandle["loanId"])

    private val _state = MutableStateFlow(LoanPaymentState())
    val state: StateFlow<LoanPaymentState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanPaymentNavigationEvent>()
    val navigationEvent: SharedFlow<LoanPaymentNavigationEvent> = _navigationEvent

    fun showAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = true) }
    }

    fun hideAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = false) }
    }

    fun onAccountClicked(selectedAccount: Account) {
        _state.update { it.copy(selectedAccount = selectedAccount) }
    }

    fun onAmountChange(amount: Int) {
        _state.update { it.copy(amount = amount) }
        validateFields()
    }

    fun onPayClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanPaymentNavigationEvent.NavigateToSuccessfulLoanPayment)
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.amount != 0
            )
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanPaymentNavigationEvent.NavigateBack)
        }
    }
}