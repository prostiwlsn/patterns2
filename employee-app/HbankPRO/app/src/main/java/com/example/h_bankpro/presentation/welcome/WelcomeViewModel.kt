package com.example.h_bankpro.presentation.welcome

import com.example.h_bankpro.data.FirebasePushManager
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.SaveTokenUseCase
import com.example.h_bankpro.domain.useCase.SendFcmTokenUseCase
import com.example.h_bankpro.domain.useCase.SettingsUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class WelcomeViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val settingsUseCase: SettingsUseCase,
    private val firebasePushManager: FirebasePushManager,
    private val sendFcmTokenUseCase: SendFcmTokenUseCase
) : BaseViewModel() {
    private val _state = MutableStateFlow(WelcomeState())
    val state: StateFlow<WelcomeState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<WelcomeNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onLogin() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _navigationEvent.emit(WelcomeNavigationEvent.NavigateToLogin)
        }
    }

    fun onRegister() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _navigationEvent.emit(WelcomeNavigationEvent.NavigateToRegister)
        }
    }

    fun handleAuthCallback(
        accessToken: String?,
        refreshToken: String?,
        isRegistration: Boolean = false
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                    saveTokenUseCase(accessToken, refreshToken)
                    settingsUseCase.getSettings()
                    if (isRegistration) {
                        val fcmToken = firebasePushManager.getFcmToken()
                        if (fcmToken != null) {
                            val userId = UUID.randomUUID().toString()
                            when (val tokenResult =
                                sendFcmTokenUseCase(userId, isManager = true, fcmToken)) {
                                is RequestResult.Success -> {
                                }

                                is RequestResult.Error -> {
                                    pushCommandUseCase(Command.NavigateToServerError)
                                    _state.update { it.copy(isLoading = false) }
                                    return@launch
                                }

                                is RequestResult.NoInternetConnection -> {
                                    pushCommandUseCase(Command.NavigateToNoConnection)
                                    _state.update { it.copy(isLoading = false) }
                                    return@launch
                                }
                            }
                        } else {
                            pushCommandUseCase(Command.NavigateToServerError)
                            _state.update { it.copy(isLoading = false) }
                            return@launch
                        }
                    }
                    _state.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(WelcomeNavigationEvent.NavigateToMain)
                } else {
                    _state.update { it.copy(isLoading = false) }
                    pushCommandUseCase(Command.NavigateToServerError)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                pushCommandUseCase(Command.NavigateToServerError)
            }
        }
    }
}