package com.example.h_bank.presentation.registration

data class RegistrationState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isRepeatPasswordVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
)