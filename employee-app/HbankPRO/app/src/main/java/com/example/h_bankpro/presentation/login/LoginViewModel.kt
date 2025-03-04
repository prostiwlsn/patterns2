package com.example.h_bankpro.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.useCase.LoginUseCase
import com.example.h_bankpro.domain.useCase.LoginValidationUseCase
import com.example.h_bankpro.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bankpro.domain.useCase.storage.ResetCredentialsUseCase
import com.example.h_bankpro.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bankpro.presentation.login.model.LoginFrontErrors
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val updateCredentialsUseCase: UpdateCredentialsUseCase,
    private val validationUseCase: LoginValidationUseCase,
    private val loginUseCase: LoginUseCase,
    getCredentialsFlowUseCase: GetCredentialsFlowUseCase,
    resetCredentialsUseCase: ResetCredentialsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent: SharedFlow<LoginNavigationEvent> = _navigationEvent

    init {
        resetCredentialsUseCase()
        getCredentialsFlowUseCase().onEach { credentials ->
            _state.update {
                it.copy(
                    login = credentials.phoneNumber.orEmpty(),
                    password = credentials.password.orEmpty(),
                    areFieldsValid = !credentials.phoneNumber.isNullOrBlank() &&
                            !credentials.password.isNullOrBlank()
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onLoginChange(login: String) = updateCredentialsUseCase { copy(phoneNumber = login) }

    fun onPasswordChange(password: String) = updateCredentialsUseCase { copy(password = password) }

    fun onLoginClicked() {
        val validationErrors = validationUseCase()
        _state.update { it.copy(fieldErorrs = validationErrors) }

        if (validationErrors == null) {
            viewModelScope.launch {
                loginUseCase()
                    .onSuccess {
                        _navigationEvent.emit(LoginNavigationEvent.NavigateToMain)
                    }
                    .onFailure {
                        if (it.code == 400) {
                            _state.update {
                                it.copy(
                                    fieldErorrs = LoginFrontErrors(
                                        loginFieldError = "",
                                        passwordFieldError = "Неверный логин или пароль",
                                    )
                                )
                            }
                        }
                    }
            }
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

    fun onChangePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !_state.value.isPasswordVisible) }
    }
}