package com.example.h_bank.presentation.navigation

import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.useCase.authorization.GetMainCommandsUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NavigationViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    getAuthorizationCommandsUseCase: GetMainCommandsUseCase,
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