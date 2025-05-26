package com.example.h_bank.presentation.welcome

import com.example.h_bank.data.FirebasePushManager
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.useCase.GetCurrentUserUseCase
import com.example.h_bank.domain.useCase.GetUserIdUseCase
import com.example.h_bank.domain.useCase.SaveTokenUseCase
import com.example.h_bank.domain.useCase.SendFcmTokenUseCase
import com.example.h_bank.domain.useCase.SettingsUseCase
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WelcomeViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val settingsUseCase: SettingsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
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
                    var userId = getUserIdUseCase()
                    if (userId == null) {
                        when (val userResult = getCurrentUserUseCase()) {
                            is RequestResult.Success -> {
                                userId = userResult.data.id
                            }

                            is RequestResult.Error -> {
                                _state.update { it.copy(isLoading = false) }
                                pushCommandUseCase(Command.NavigateToServerError)
                                return@launch
                            }

                            is RequestResult.NoInternetConnection -> {
                                _state.update { it.copy(isLoading = false) }
                                pushCommandUseCase(Command.NavigateToNoConnection)
                                return@launch
                            }
                        }
                    }
                    if (isRegistration && userId != null) {
                        val fcmToken = firebasePushManager.getFcmToken()
                        if (fcmToken != null) {
                            when (val tokenResult =
                                sendFcmTokenUseCase(userId, isManager = false, fcmToken)) {
                                is RequestResult.Success<Unit> -> {
                                }

                                is RequestResult.Error -> {
                                }

                                is RequestResult.NoInternetConnection -> {
                                }
                            }
                        } else {
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