package com.example.h_bank.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state

    private val _navigationEvent = MutableSharedFlow<RegistrationNavigationEvent>()
    val navigationEvent: SharedFlow<RegistrationNavigationEvent> = _navigationEvent

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
        validateFields()
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
        validateFields()
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
        validateFields()
    }

    fun onRepeatPasswordChange(repeatPassword: String) {
        _state.update { it.copy(repeatPassword = repeatPassword) }
        validateFields()
    }

    fun onLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(RegistrationNavigationEvent.NavigateToLogin)
        }
    }

    fun onRegister() {
        viewModelScope.launch {
            _navigationEvent.emit(RegistrationNavigationEvent.NavigateToWelcome)
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.name.isNotBlank() &&
                        _state.value.email.isNotBlank() &&
                        _state.value.password.isNotBlank() &&
                        _state.value.repeatPassword.isNotBlank()
            )
        }
    }

    fun onChangePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !_state.value.isPasswordVisible) }
    }

    fun onChangeRepeatPasswordVisibility() {
        _state.update { it.copy(isRepeatPasswordVisible = !_state.value.isRepeatPasswordVisible) }
    }
}