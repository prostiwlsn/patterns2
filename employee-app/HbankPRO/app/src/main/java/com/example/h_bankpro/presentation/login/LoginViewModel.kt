package com.example.h_bankpro.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent: SharedFlow<LoginNavigationEvent> = _navigationEvent

    fun onLoginChange(login: String) {
        _state.update { it.copy(login = login) }
        validateFields()
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
        validateFields()
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoginNavigationEvent.NavigateToMain)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoginNavigationEvent.NavigateBack)
        }
    }

    fun onRegisterClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoginNavigationEvent.NavigateToRegister)
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.login.isNotBlank() &&
                        _state.value.password.isNotBlank()
            )
        }
    }

    fun onChangePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !_state.value.isPasswordVisible) }
    }
}