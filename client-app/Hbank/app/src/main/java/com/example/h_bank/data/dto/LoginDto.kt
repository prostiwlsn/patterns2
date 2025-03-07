package com.example.h_bank.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val phone: String,
    val password: String,
)