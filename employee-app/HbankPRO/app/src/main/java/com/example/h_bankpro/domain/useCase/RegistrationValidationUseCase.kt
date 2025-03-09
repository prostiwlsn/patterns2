package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.presentation.registration.model.RegistrationFrontErrors

class RegistrationValidationUseCase(
    private val storageRepository: IAuthorizationLocalRepository,
) {
    operator fun invoke(): RegistrationFrontErrors? {
        val fieldValues = storageRepository.getCredentialsState()

        return RegistrationFrontErrors(
            nameFieldError = validateName(fieldValues.name),
            phoneNumberFieldError = validatePhoneNumber(fieldValues.phoneNumber),
            passwordFieldError = validatePassword(fieldValues.password),
            passwordConfirmationFieldError = validatePasswordConfirmation(
                fieldValues.password,
                fieldValues.passwordConfirmation,
            )
        ).takeIf { it != RegistrationFrontErrors() }
    }

    private fun validateName(name: String?): String? {
        return when {
            name.isNullOrBlank() -> "Заполните поле"
            else -> null
        }
    }

    private fun validatePhoneNumber(phoneNumber: String?): String? {
        return when {
            phoneNumber.isNullOrBlank() -> "Заполните поле"
            phoneNumber.length != 10 -> "Некорректный номер телефона"
            else -> null
        }
    }

    private fun validatePassword(password: String?): String? {
        return when {
            password.isNullOrBlank() -> "Заполните поле"
            password.length < 8 -> "Пароль должен содержать не менее 8 символов"
            password.length > 32 -> "Пароль должен содержать не более 32 символов"
            else -> null
        }
    }

    private fun validatePasswordConfirmation(
        password: String?,
        passwordConfirmation: String?
    ): String? {
        return when {
            passwordConfirmation.isNullOrBlank() -> "Заполните поле"
            password != passwordConfirmation -> "Пароль и его повтор не совпадают"
            else -> null
        }
    }
}