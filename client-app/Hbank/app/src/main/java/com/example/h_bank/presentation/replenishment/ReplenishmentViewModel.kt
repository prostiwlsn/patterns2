package com.example.h_bank.presentation.replenishment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import com.example.h_bank.data.utils.NetworkUtils.onFailure
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.ReplenishUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReplenishmentViewModel(
    savedStateHandle: SavedStateHandle,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val replenishUseCase: ReplenishUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ReplenishmentState())
    val state: StateFlow<ReplenishmentState> = _state

    private val _navigationEvent = MutableSharedFlow<ReplenishmentNavigationEvent>()
    val navigationEvent: SharedFlow<ReplenishmentNavigationEvent> = _navigationEvent

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    init {
        loadUserAccounts()
    }

    private fun loadUserAccounts() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getUserAccountsUseCase(userId)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            accounts = result.data,
                            isLoading = false,
                            selectedAccount = result.data.first()
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onAmountChange(amountInput: String) {
        val amountValue = amountInput.toDoubleOrNull()

        if (amountValue != null || amountInput.isEmpty()) {
            _state.update { it.copy(amount = amountValue) }
        }

        _state.update { it.copy(amount = amountValue) }
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
                areFieldsValid = _state.value.selectedAccount != null
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

    fun onReplenishClicked() {
        viewModelScope.launch {
            state.value.amount?.let {
                replenishUseCase(
                    state.value.selectedAccount?.id ?: "",
                    it
                ).onSuccess {
                    _navigationEvent.emit(
                        ReplenishmentNavigationEvent.NavigateToSuccessfulReplenishment(
                            accountNumber = _state.value.selectedAccount?.accountNumber ?: "",
                            amount = _state.value.amount.toString()
                        )
                    )
                }
                    .onFailure { }
            }
        }
    }
}
