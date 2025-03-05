package com.example.h_bankpro.data.dto

import kotlinx.serialization.Serializable

@Serializable
class RegisterDto(
    val phone: String,
    val password: String,
    val name: String,
)