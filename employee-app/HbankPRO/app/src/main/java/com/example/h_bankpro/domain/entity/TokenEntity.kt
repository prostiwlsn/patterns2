package com.example.h_bankpro.domain.entity

data class TokenEntity(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val expiresAt: Long? = null
)