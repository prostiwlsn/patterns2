package com.example.h_bank.presentation.loanPayment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import com.example.h_bank.data.utils.NetworkUtils.onFailure
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.domain.useCase.GetCurrentUserUseCase
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanPaymentViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
) : ViewModel() {
    private val loanId: String = checkNotNull(savedStateHandle["loanId"])

    private val _state = MutableStateFlow(LoanPaymentState())
    val state: StateFlow<LoanPaymentState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanPaymentNavigationEvent>()
    val navigationEvent: SharedFlow<LoanPaymentNavigationEvent> = _navigationEvent

    init {
        loadCurrentUser()
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

    fun onAmountChange(amount: Int) {
        _state.update { it.copy(amount = amount) }
        validateFields()
    }

    private fun loadCurrentUser() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { result ->
                _state.update { it.copy(currentUserId = result.data.id) }
                loadUserAccounts()
            }
                .onFailure { }
        }
    }

    private fun loadUserAccounts() {
        viewModelScope.launch {
            getUserAccountsUseCase(state.value.currentUserId)
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