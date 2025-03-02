package com.example.h_bank.presentation.replenishment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReplenishmentViewModel : ViewModel() {
    private val _state = MutableStateFlow(ReplenishmentState())
    val state: StateFlow<ReplenishmentState> = _state

    private val _navigationEvent = MutableSharedFlow<ReplenishmentNavigationEvent>()
    val navigationEvent: SharedFlow<ReplenishmentNavigationEvent> = _navigationEvent

    fun onAmountChange(amount: Long) {
        _state.update { it.copy(amount = amount) }
        validateFields()
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(ReplenishmentNavigationEvent.NavigateBack)
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.amount > 0 &&
                        _state.value.amount <= _state.value.selectedAccount.balance
            )
        }
    }

    fun showAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = true) }
    }

    fun hideAccountsSheet() {
        _state.update { it.copy(isAccountsSheetVisible = false) }
    }

    fun onAccountClicked(selectedAccount: Account) {
        _state.update { it.copy(selectedAccount = selectedAccount) }
    }

    fun onWithdrawClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                ReplenishmentNavigationEvent.NavigateToSuccessfulReplenishment(
                    accountId = _state.value.selectedAccount.name,
                    amount = _state.value.amount
                )
            )
        }
    }
}