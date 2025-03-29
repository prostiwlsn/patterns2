package com.example.h_bank.presentation.welcome

import com.example.h_bank.domain.useCase.SaveTokenUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(WelcomeState())
    val state: StateFlow<WelcomeState> = _state

    private val _navigationEvent = MutableSharedFlow<WelcomeNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(WelcomeNavigationEvent.NavigateToLogin)
        }
    }

    fun onRegister() {
        viewModelScope.launch {
            _navigationEvent.emit(WelcomeNavigationEvent.NavigateToRegister)
        }
    }

    fun handleAuthCallback(accessToken: String?, refreshToken: String?) {
        viewModelScope.launch {
            if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                saveTokenUseCase(accessToken, refreshToken)
                _navigationEvent.emit(WelcomeNavigationEvent.NavigateToMain)
            } else {
            }
        }
    }
}