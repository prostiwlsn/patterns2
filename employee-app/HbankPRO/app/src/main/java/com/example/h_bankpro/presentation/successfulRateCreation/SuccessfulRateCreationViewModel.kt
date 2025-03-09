package com.example.h_bankpro.presentation.successfulRateCreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulRateCreationViewModel(
    override val pushCommandUseCase: PushCommandUseCase
) : BaseViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulRateCreationNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulRateCreationNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulRateCreationNavigationEvent.NavigateToMain)
        }
    }
}