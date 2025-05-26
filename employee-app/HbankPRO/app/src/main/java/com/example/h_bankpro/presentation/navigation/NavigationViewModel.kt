package com.example.h_bankpro.presentation.navigation

import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.useCase.GetCommandUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NavigationViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    getAuthorizationCommandsUseCase: GetCommandUseCase,
) : BaseViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        getAuthorizationCommandsUseCase().onEach { command ->
            when (command) {
                is Command.NavigateToNoConnection ->
                    _navigationEvent.emit(NavigationEvent.NavigateToNoConnection)

                is Command.NavigateToServerError ->
                    _navigationEvent.emit(NavigationEvent.NavigateToServerError)

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }
}