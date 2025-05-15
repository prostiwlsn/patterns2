package com.example.h_bank.presentation.launch

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.useCase.SettingsUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.domain.useCase.authorization.RefreshTokenUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LaunchViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val settingsUseCase: SettingsUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(LaunchState())
    val state: StateFlow<LaunchState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LaunchNavigationEvent>(replay = 1)
    val navigationEvent: SharedFlow<LaunchNavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        checkAndRefreshToken()
    }

    private fun checkAndRefreshToken() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                when (val result = refreshTokenUseCase()) {
                    is RequestResult.Success -> {
                        settingsUseCase.getSettings()
                        _state.update { it.copy(isLoading = false, isTokenValid = true) }
                        _navigationEvent.emit(LaunchNavigationEvent.NavigateToMain)
                    }

                    is RequestResult.Error -> {
                        println("Refresh token error: ${result.code}, ${result.message}")
                        _state.update { it.copy(isLoading = false, isTokenValid = false) }
                        _navigationEvent.emit(LaunchNavigationEvent.NavigateToWelcome)
                    }

                    is RequestResult.NoInternetConnection -> {
                        println("No internet connection during refresh token")
                        _state.update { it.copy(isLoading = false, isTokenValid = false) }
                        pushCommandUseCase(Command.NavigateToNoConnection)
                    }
                }
            } catch (e: Exception) {
                println("Unexpected error during refresh token: ${e.message}")
                _state.update { it.copy(isLoading = false, isTokenValid = false) }
                throw e // Перебрасываем в CoroutineExceptionHandler
            }
        }
    }
}