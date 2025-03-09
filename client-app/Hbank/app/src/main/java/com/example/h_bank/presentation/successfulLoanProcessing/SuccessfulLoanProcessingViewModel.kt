package com.example.h_bank.presentation.successfulLoanProcessing

import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulLoanProcessingViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
) : BaseViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulLoanProcessingNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulLoanProcessingNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulLoanProcessingNavigationEvent.NavigateToMain)
        }
    }
}