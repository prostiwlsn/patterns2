package com.example.h_bankpro.presentation.userCreation

import com.example.h_bankpro.data.RoleType
import com.example.h_bankpro.data.dto.UserCreationDto
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.useCase.CreateUserUseCase
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserCreationViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val createUserUseCase: CreateUserUseCase
) : BaseViewModel() {
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
                        state.value.password.isNotBlank()
            )
        }
    }

    fun onCreateClicked() {
        viewModelScope.launch {
            val request = UserCreationDto(
                phone = state.value.phone,
                password = state.value.password,
                name = state.value.name,
                role = if (state.value.selectedRole) null else RoleType.MANAGER
            )
            createUserUseCase(request = request)
                .onSuccess {
                    pushCommandUseCase(Command.RefreshMainScreen)
                    _navigationEvent.emit(
                        UserCreationNavigationEvent.NavigateToSuccessfulUserCreation
                    )
                }
        }
    }
}