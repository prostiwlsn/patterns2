package com.example.h_bank.presentation.registration.model

data class RegistrationFrontErrors(
    val nameFieldError: String? = null,
    val phoneNumberFieldError: String? = null,
    val passwordFieldError: String? = null,
    val passwordConfirmationFieldError: String? = null,
)