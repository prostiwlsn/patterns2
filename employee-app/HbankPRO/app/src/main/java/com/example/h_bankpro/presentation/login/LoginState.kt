package com.example.h_bankpro.presentation.login

import com.example.h_bankpro.presentation.login.model.LoginFrontErrors

data class LoginState(
    val login: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val areFieldsValid: Boolean = false,
    val fieldErorrs: LoginFrontErrors? = null,
)