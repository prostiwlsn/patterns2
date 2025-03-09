package com.example.h_bankpro.presentation.loan

import androidx.lifecycle.SavedStateHandle
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val _state = MutableStateFlow(LoanState())
    val state: StateFlow<LoanState> = _state

    private val _navigationEvent = MutableSharedFlow<LoanNavigationEvent>()
    val navigationEvent: SharedFlow<LoanNavigationEvent> = _navigationEvent

    private val documentNumber: String = checkNotNull(savedStateHandle["documentNumber"])
    private val amount: String = checkNotNull(savedStateHandle["amount"])
    private val endDate: String = checkNotNull(savedStateHandle["endDate"])
    private val ratePercent: String = checkNotNull(savedStateHandle["ratePercent"])
    private val debt: String = checkNotNull(savedStateHandle["debt"])

    init {
        _state.update {
            it.copy(
                documentNumber = documentNumber,
                amount = amount,
                endDate = endDate,
                ratePercent = ratePercent,
                debt = debt,
            )
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoanNavigationEvent.NavigateBack)
        }
    }
}