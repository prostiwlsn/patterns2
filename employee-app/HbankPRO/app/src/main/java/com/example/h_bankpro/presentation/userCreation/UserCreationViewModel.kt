package com.example.h_bankpro.presentation.userCreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserCreationViewModel : ViewModel() {
    private val _state = MutableStateFlow(UserCreationState())
    val state: StateFlow<UserCreationState> = _state

    private val _navigationEvent = MutableSharedFlow<UserCreationNavigationEvent>()
    val navigationEvent: SharedFlow<UserCreationNavigationEvent> = _navigationEvent

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
        validateFields()
    }

    fun onPhoneChange(phone: String) {
        _state.update { it.copy(phone = phone) }
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

    fun onRoleSelect(role: Boolean) {
        _state.update { it.copy(selectedRole = role) }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(UserCreationNavigationEvent.NavigateBack)
        }
    }

    private fun validateFields() {
        _state.update {
            it.copy(
                areFieldsValid = _state.value.name.isNotBlank() &&
                        state.value.phone.isNotBlank() &&
                        state.value.password.isNotBlank() &&
                        state.value.repeatPassword.isNotBlank()
            )
        }
    }

    fun onCreateClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(
                UserCreationNavigationEvent.NavigateToSuccessfulUserCreation
            )
        }
    }
}