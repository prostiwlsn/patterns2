package com.example.h_bankpro.presentation.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state

    private val _navigationEvent = MutableSharedFlow<UserNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val userId: String = checkNotNull(savedStateHandle["userId"])

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

    fun onLoanClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(UserNavigationEvent.NavigateToLoan)
        }
    }

    fun onAccountClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(UserNavigationEvent.NavigateToAccount)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(UserNavigationEvent.NavigateBack)
        }
    }

    fun onBlockUserClicked() {
    }
}