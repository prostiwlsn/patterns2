package com.example.h_bankpro.domain.entity

data class CredentialsEntity(
    val phoneNumber: String? = null,
    val password: String? = null,
    val passwordConfirmation: String? = null,
    val name: String? = null,
)