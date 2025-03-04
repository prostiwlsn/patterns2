package com.example.h_bankpro.presentation.registration

import com.example.h_bankpro.presentation.registration.model.RegistrationFrontErrors

data class RegistrationState(
    val phoneNumber: String = "",
    val name: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isRepeatPasswordVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
    val fieldErrors: RegistrationFrontErrors? = null,
)