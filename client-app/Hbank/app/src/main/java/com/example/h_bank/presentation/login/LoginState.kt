package com.example.h_bank.presentation.login

data class LoginState(
    val login: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
)