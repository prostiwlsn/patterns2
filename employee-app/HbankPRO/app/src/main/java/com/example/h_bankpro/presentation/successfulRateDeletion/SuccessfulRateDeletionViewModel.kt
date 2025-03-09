package com.example.h_bankpro.presentation.successfulRateDeletion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SuccessfulRateDeletionViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
) : BaseViewModel() {
    private val _navigationEvent = MutableSharedFlow<SuccessfulRateDeletionNavigationEvent>()
    val navigationEvent: SharedFlow<SuccessfulRateDeletionNavigationEvent> = _navigationEvent

    fun onToMainClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SuccessfulRateDeletionNavigationEvent.NavigateToMain)
        }
    }
}