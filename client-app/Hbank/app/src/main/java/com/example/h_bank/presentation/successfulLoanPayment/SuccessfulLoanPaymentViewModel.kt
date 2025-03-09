package com.example.h_bank.presentation.successfulLoanPayment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import com.example.h_bank.presentation.loanPayment.LoanPaymentState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SuccessfulLoanPaymentViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val _state = MutableStateFlow(SuccessfulLoanPaymentState())
    val state: StateFlow<SuccessfulLoanPaymentState> = _state

    private val _navigationEvent = MutableSharedFlow<SuccessfulLoanPaymentNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulLoanPaymentNavigationEvent> = _navigationEvent

    private val amount: String = checkNotNull(savedStateHandle["amount"])
    private val documentNumber: String = checkNotNull(savedStateHandle["documentNumber"])
    private val debt: String = checkNotNull(savedStateHandle["debt"])

    init {
        _state.update { it.copy(amount = amount, documentNumber = documentNumber, debt = debt) }
    }

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulLoanPaymentNavigationEvent.NavigateToMain)
        }
    }
}