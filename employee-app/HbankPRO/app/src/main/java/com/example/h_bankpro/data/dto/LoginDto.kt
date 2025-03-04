package com.example.h_bankpro.data.dto

import kotlinx.serialization.Serializable

@Serializable
class LoginDto(
    val phone: String,
    val password: String,
)