package com.example.h_bankpro.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.h_bankpro.data.utils.NetworkUtils.onFailure
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.domain.entity.Command
import com.example.h_bankpro.domain.useCase.PushCommandUseCase
import com.example.h_bankpro.domain.useCase.RegisterUseCase
import com.example.h_bankpro.domain.useCase.RegistrationValidationUseCase
import com.example.h_bankpro.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bankpro.domain.useCase.storage.ResetCredentialsUseCase
import com.example.h_bankpro.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bankpro.presentation.common.viewModel.BaseViewModel
import com.example.h_bankpro.presentation.registration.model.RegistrationFrontErrors
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    override val pushCommandUseCase: PushCommandUseCase,
    private val updateCredentialsUseCase: UpdateCredentialsUseCase,
    private val validationUseCase: RegistrationValidationUseCase,
    private val registerUseCase: RegisterUseCase,
    getCredentialsFlowUseCase: GetCredentialsFlowUseCase,
    resetCredentialsUseCase: ResetCredentialsUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state

    private val _navigationEvent = MutableSharedFlow<RegistrationNavigationEvent>()
    val navigationEvent: SharedFlow<RegistrationNavigationEvent> = _navigationEvent

    init {
        resetCredentialsUseCase()
        getCredentialsFlowUseCase().onEach { credentials ->
            _state.update {
                it.copy(
                    phoneNumber = credentials.phoneNumber.orEmpty(),
                    name = credentials.name.orEmpty(),
                    password = credentials.password.orEmpty(),
                    repeatPassword = credentials.passwordConfirmation.orEmpty(),
                    areFieldsValid = !credentials.name.isNullOrBlank() &&
                            !credentials.phoneNumber.isNullOrBlank() &&
                            !credentials.password.isNullOrBlank() &&
                            !credentials.passwordConfirmation.isNullOrBlank()
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onNameChange(name: String) = updateCredentialsUseCase { copy(name = name) }

    fun onPhoneNumberChange(phoneNumber: String) =
        updateCredentialsUseCase { copy(phoneNumber = phoneNumber) }

    fun onPasswordChange(password: String) = updateCredentialsUseCase { copy(password = password) }

    fun onRepeatPasswordChange(repeatPassword: String) =
        updateCredentialsUseCase { copy(passwordConfirmation = repeatPassword) }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(RegistrationNavigationEvent.NavigateToLogin)
        }
    }

    fun onRegisterClicked() {
        val validationErrors = validationUseCase()

        _state.update {
            it.copy(fieldErrors = validationErrors)
        }

        if (validationErrors == null) {
            viewModelScope.launch {
                registerUseCase()
                    .onSuccess {
                        pushCommandUseCase(Command.RefreshMainScreen)
                        _navigationEvent.emit(RegistrationNavigationEvent.NavigateToMain)
                    }
                    .onFailure {
                        if (it.code == 400) {
                            _state.update {
                                it.copy(
                                    fieldErrors = RegistrationFrontErrors(
                                        phoneNumberFieldError =
                                        "Пользователь с таким номером уже существует",
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
            _navigationEvent.emit(RegistrationNavigationEvent.NavigateBack)
        }
    }

    fun onChangePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !_state.value.isPasswordVisible) }
    }

    fun onChangeRepeatPasswordVisibility() {
        _state.update { it.copy(isRepeatPasswordVisible = !_state.value.isRepeatPasswordVisible) }
    }
}