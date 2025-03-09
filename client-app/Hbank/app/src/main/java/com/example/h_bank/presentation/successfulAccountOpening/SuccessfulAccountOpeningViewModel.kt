package com.example.h_bank.presentation.successfulAccountOpening

import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import com.example.h_bank.presentation.successfulAccountClosure.SuccessfulAccountClosureNavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulAccountOpeningViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
) : BaseViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulAccountClosureNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulAccountClosureNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulAccountClosureNavigationEvent.NavigateToMain)
        }
    }
}