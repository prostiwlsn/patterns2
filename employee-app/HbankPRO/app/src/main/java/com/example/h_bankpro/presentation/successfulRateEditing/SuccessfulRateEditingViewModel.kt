package com.example.h_bankpro.presentation.successfulRateEditing

import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulRateEditingViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
) : BaseViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulRateEditingNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulRateEditingNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulRateEditingNavigationEvent.NavigateToMain)
        }
    }
}