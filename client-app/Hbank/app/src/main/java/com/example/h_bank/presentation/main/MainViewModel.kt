package com.example.h_bank.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import com.example.h_bank.data.Loan
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    private val _navigationEvent = MutableSharedFlow<MainNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun showAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = true) }
    }

    fun hideAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = false) }
    }

    fun showLoansSheet() {
        _state.update { it.copy(isLoansSheetVisible = true) }
    }

    fun hideLoansSheet() {
        _state.update { it.copy(isLoansSheetVisible = false) }
    }

    fun onAccountClicked(account: Account) {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToAccount(account.id))
        }
    }

    fun onLoanClicked(loan: Loan) {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToLoan(loan.id.toString()))
        }
    }

    fun onTransferClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToTransfer)
        }
    }

    fun onHistoryClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToHistory)
        }
    }

    fun onProcessLoanClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToLoanProcessing)
        }
    }

    fun onOpenAccountClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(MainNavigationEvent.NavigateToSuccessfulAccountOpening)
        }
    }
}