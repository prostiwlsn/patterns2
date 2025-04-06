package com.example.h_bankpro.presentation.welcome

import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.SaveTokenUseCase
import com.example.h_bankpro.domain.useCase.SettingsUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val settingsUseCase: SettingsUseCase
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
                settingsUseCase.getSettings()
                _navigationEvent.emit(WelcomeNavigationEvent.NavigateToMain)
            } else {
            }
        }
    }
}