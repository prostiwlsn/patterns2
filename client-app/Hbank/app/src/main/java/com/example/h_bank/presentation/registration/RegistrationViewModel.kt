package com.example.h_bank.presentation.registration

import com.example.h_bank.data.utils.NetworkUtils.onFailure
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.domain.entity.authorization.Command
import com.example.h_bank.domain.useCase.authorization.PushCommandUseCase
import com.example.h_bank.domain.useCase.authorization.RegisterUseCase
import com.example.h_bank.domain.useCase.authorization.RegistrationValidationUseCase
import com.example.h_bank.domain.useCase.storage.GetCredentialsFlowUseCase
import com.example.h_bank.domain.useCase.storage.ResetCredentialsUseCase
import com.example.h_bank.domain.useCase.storage.UpdateCredentialsUseCase
import com.example.h_bank.presentation.common.viewModelBase.BaseViewModel
import com.example.h_bank.presentation.registration.model.RegistrationFrontErrors
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
    private val pushAuthorizationCommandUseCase: PushCommandUseCase,
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
                        pushAuthorizationCommandUseCase(Command.UpdateProfile)
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