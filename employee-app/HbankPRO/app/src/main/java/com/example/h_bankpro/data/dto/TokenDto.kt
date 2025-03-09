package com.example.h_bankpro.data.dto

import kotlinx.serialization.Serializable

@Serializable
class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int? = null
)