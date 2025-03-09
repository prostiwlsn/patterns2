package com.example.h_bankpro.presentation.userCreation

data class UserCreationState(
    val name: String = "",
    val phone: String = "",
    val password: String = "",
    val selectedRole: Boolean = false,
    val areFieldsValid: Boolean = false,
)