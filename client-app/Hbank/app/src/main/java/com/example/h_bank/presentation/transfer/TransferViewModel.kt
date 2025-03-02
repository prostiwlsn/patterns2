package com.example.h_bank.presentation.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferViewModel : ViewModel() {
    private val _state = MutableStateFlow(TransferState())
    val state: StateFlow<TransferState> = _state

    private val _navigationEvent = MutableSharedFlow<TransferNavigationEvent>()
    val navigationEvent: SharedFlow<TransferNavigationEvent> = _navigationEvent

    fun onAmountChange(amount: Long) {
        _state.update { it.copy(amount = amount) }
        validateFields()
    }

    fun onBeneficiaryAccountIdChange(id: String) {
        _state.update { it.copy(beneficiaryAccountId = id) }
        validateFields()
    }

    fun onCommentChange(comment: String) {
        _state.update { it.copy(comment = comment) }
    }


    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(TransferNavigationEvent.NavigateBack)
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.amount > 0 &&
                        _state.value.amount <= _state.value.selectedAccount.balance &&
                        _state.value.beneficiaryAccountId.isNotBlank()
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

    fun onPayClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                TransferNavigationEvent.NavigateToSuccessfulTransfer(
                    accountId = _state.value.selectedAccount.id,
                    beneficiaryAccountId = _state.value.beneficiaryAccountId,
                    amount = _state.value.amount
                )
            )
        }
    }
}