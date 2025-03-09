package com.example.h_bank.domain.useCase.authorization

import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.presentation.login.model.LoginFrontErrors

class LoginValidationUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke(): LoginFrontErrors? {
        return LoginFrontErrors(
            loginFieldError = validateLoginField(),
            passwordFieldError = validatePasswordField(),
        ).takeIf { it != LoginFrontErrors() }
    }

    private fun validateLoginField(): String? {
        val fieldValue = storageRepository.getCredentialsState().phoneNumber

        return when {
            fieldValue == null -> "Заполните поле"
            fieldValue.length != 10 -> "Некорректный номер телефона"
            else -> null
        }
    }

    private fun validatePasswordField(): String? {
        val fieldValue = storageRepository.getCredentialsState().password

        return when {
            fieldValue == null -> "Заполните поле"
            fieldValue.length < 8 -> "Пароль должен содержать не менее 8 символов"
            fieldValue.length > 32 -> "Пароль должен содержать не более 32 символов"
            else -> null
        }
    }
}