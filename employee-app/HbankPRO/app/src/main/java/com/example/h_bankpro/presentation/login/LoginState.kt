package com.example.h_bankpro.presentation.login

data class LoginState(
    val login: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
)