package com.example.h_bank.presentation.loanProcessing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bank.data.Rate
import com.example.h_bank.domain.useCase.loan.GetLoanFlowUseCase
import com.example.h_bank.domain.useCase.loan.GetTariffListUseCase
import com.example.h_bank.domain.useCase.loan.LoanProcessingValidationUseCase
import com.example.h_bank.domain.useCase.loan.UpdateLoanUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanProcessingViewModel(
    private val updateLoanUseCase: UpdateLoanUseCase,
    private val loanProcessingValidationUseCase: LoanProcessingValidationUseCase,
    private val getTariffListUseCase: GetTariffListUseCase,
    getLoanFlowUseCase: GetLoanFlowUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LoanProcessingState())
    val state: StateFlow<LoanProcessingState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanProcessingNavigationEvent>()
    val navigationEvent: SharedFlow<LoanProcessingNavigationEvent> = _navigationEvent

    init {
        getLoanFlowUseCase().onEach { loan ->
            _state.update {
                it.copy(
                    amount = loan.amount,
                    term = loan.duration,
                    interestRate = loan.rate,
                    dailyPayment = loan.dailyPayment,
                    areFieldsValid = loan.amount != null && loan.duration != null,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAmountChange(amountInput: String) {
        val amountValue = amountInput.toIntOrNull()

        if (amountValue != null && amountValue <= 10_000_000 || amountInput.isEmpty()) {
            updateLoanUseCase { copy(amount = amountValue) }
        }
    }

    fun onTermChange(termInput: String) {
        val termValue = termInput.toIntOrNull()

        if (termValue != null && termValue < 10 || termInput.isEmpty()) {
            updateLoanUseCase { copy(duration = termValue) }
        }
    }

    fun onRateClicked(selectedRate: Rate) {
        _state.update { it.copy(selectedRate = selectedRate) }
    }

    fun onProcessLoanClicked() {
        val fieldErrors = loanProcessingValidationUseCase()

        _state.update {
            it.copy(
                fieldErrors = fieldErrors,
            )
        }

        if (fieldErrors == null) {
            viewModelScope.launch {
                _navigationEvent.emit(LoanProcessingNavigationEvent.NavigateToSuccessfulLoanProcessing)
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanProcessingNavigationEvent.NavigateBack)
        }
    }

    fun showRatesSheet() {
        _state.update { it.copy(isRatesSheetVisible = true) }
    }

    fun hideRatesSheet() {
        _state.update { it.copy(isRatesSheetVisible = false) }
    }
}