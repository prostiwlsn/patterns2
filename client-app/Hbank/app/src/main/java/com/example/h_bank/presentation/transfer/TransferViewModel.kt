package com.example.h_bank.presentation.transfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Account
import com.example.h_bank.data.utils.NetworkUtils.onFailure
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.useCase.GetAccountIdByNumberUseCase
import com.example.h_bank.domain.useCase.GetUserAccountsUseCase
import com.example.h_bank.domain.useCase.TransferUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val transferUseCase: TransferUseCase,
    private val getAccountIdByNumberUseCase: GetAccountIdByNumberUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(TransferState())
    val state: StateFlow<TransferState> = _state

    private val _navigationEvent = MutableSharedFlow<TransferNavigationEvent>()
    val navigationEvent: SharedFlow<TransferNavigationEvent> = _navigationEvent

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

    fun onBeneficiaryAccountIdChange(accountNumber: String) {
        val formattedAccountNumber = formatAccountNumber(accountNumber)
        _state.update { it.copy(beneficiaryAccountNumber = formattedAccountNumber) }
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

    private suspend fun getBeneficiaryAccountId(): RequestResult<String> {
        return getAccountIdByNumberUseCase(state.value.beneficiaryAccountNumber)
    }

    fun onPayClicked() {
        viewModelScope.launch {
            val beneficiaryAccountIdResult = getBeneficiaryAccountId()

            beneficiaryAccountIdResult.onSuccess { result ->
                val beneficiaryAccountId = result.data

                _state.update { it.copy(beneficiaryAccountId = beneficiaryAccountId) }

                state.value.amount?.let {
                    transferUseCase(
                        state.value.selectedAccount?.id ?: "",
                        beneficiaryAccountId,
                        it,
                        state.value.comment
                    ).onSuccess {
                        _navigationEvent.emit(
                            TransferNavigationEvent.NavigateToSuccessfulTransfer(
                                accountNumber = _state.value.selectedAccount?.accountNumber ?: "",
                                beneficiaryAccountNumber = _state.value.beneficiaryAccountNumber,
                                amount = _state.value.amount.toString()
                            )
                        )
                    }.onFailure {
                    }
                }
            }.onFailure {
            }
        }
    }

    private fun formatAccountNumber(input: String): String {
        val cleanedInput = input.replace("\\D".toRegex(), "")
        val formattedInput = StringBuilder()

        for (i in cleanedInput.indices) {
            if (i > 0 && i % 4 == 0) {
                formattedInput.append(" ")
            }
            formattedInput.append(cleanedInput[i])
        }
        return formattedInput.toString()
    }
}
