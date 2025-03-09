package com.example.h_bank.domain.entity.authorization

data class CredentialsEntity(
    val userId: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val passwordConfirmation: String? = null,
    val name: String? = null,
)