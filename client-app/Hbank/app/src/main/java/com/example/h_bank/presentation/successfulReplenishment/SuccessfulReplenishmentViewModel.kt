package com.example.h_bank.presentation.successfulReplenishment

import androidx.lifecycle.SavedStateHandle
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SuccessfulReplenishmentViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val _state = MutableStateFlow(SuccessfulReplenishmentState())
    val state: StateFlow<SuccessfulReplenishmentState> = _state

    private val _navigationEvent = MutableSharedFlow<SuccessfulReplenishmentNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulReplenishmentNavigationEvent> = _navigationEvent

    private val amount: String = checkNotNull(savedStateHandle["amount"])
    private val accountNumber: String = checkNotNull(savedStateHandle["accountNumber"])
    private val currency: String = checkNotNull(savedStateHandle["currency"])

    init {
        _state.update {
            it.copy(
                accountNumber = accountNumber,
                amount = amount,
                currency = currency
            )
        }
    }

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulReplenishmentNavigationEvent.NavigateToMain)
        }
    }
}