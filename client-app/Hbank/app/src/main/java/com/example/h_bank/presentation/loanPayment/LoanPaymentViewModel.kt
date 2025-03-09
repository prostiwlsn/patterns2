package com.example.h_bank.presentation.loanPayment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import com.example.h_bank.data.utils.NetworkUtils.onFailure
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.domain.useCase.GetCurrentUserUseCase
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.RepayLoanUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanPaymentViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val repayLoanUseCase: RepayLoanUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(LoanPaymentState())
    val state: StateFlow<LoanPaymentState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanPaymentNavigationEvent>()
    val navigationEvent: SharedFlow<LoanPaymentNavigationEvent> = _navigationEvent

    private val loanId: String = checkNotNull(savedStateHandle["loanId"])
    private val documentNumber: String = checkNotNull(savedStateHandle["documentNumber"])
    private val debt: String = checkNotNull(savedStateHandle["debt"])

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

    fun onAmountChange(amountInput: String) {
        val amountValue = amountInput.toDoubleOrNull()

        if (amountValue != null || amountInput.isEmpty()) {
            _state.update { it.copy(amount = amountValue) }
        }

        _state.update { it.copy(amount = amountValue) }
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
            state.value.amount?.let {
                repayLoanUseCase(
                    state.value.selectedAccount?.id ?: "",
                    loanId,
                    it
                ).onSuccess {
                    _navigationEvent.emit(
                        LoanPaymentNavigationEvent.NavigateToSuccessfulLoanPayment(
                            documentNumber = documentNumber,
                            amount = _state.value.amount.toString(),
                            debt = (((debt.toDouble() - (_state.value.amount ?: 0.0)))).toString(),
                            accountNumber = _state.value.selectedAccount?.accountNumber ?: "",
                        )
                    )
                }
                    .onFailure { }
            }
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.selectedAccount != null
            )
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanPaymentNavigationEvent.NavigateBack)
        }
    }
}