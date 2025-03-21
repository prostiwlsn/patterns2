package com.example.h_bankpro.presentation.launch

import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.RefreshTokenUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
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
    private val refreshTokenUseCase: RefreshTokenUseCase
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
            when (val result = refreshTokenUseCase()) {
                is RequestResult.Success -> {
                    _state.update { it.copy(isLoading = false, isTokenValid = true) }
                    _navigationEvent.emit(LaunchNavigationEvent.NavigateToMain)
                }

                is RequestResult.Error -> {
                    _state.update { it.copy(isLoading = false, isTokenValid = false) }
                    _navigationEvent.emit(LaunchNavigationEvent.NavigateToWelcome)
                }

                is RequestResult.NoInternetConnection -> {
                    _state.update { it.copy(isLoading = false, isTokenValid = false) }
                    _navigationEvent.emit(LaunchNavigationEvent.NavigateToWelcome)
                }
            }
        }
    }
}
