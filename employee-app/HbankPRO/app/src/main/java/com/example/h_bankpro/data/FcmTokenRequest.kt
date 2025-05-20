package com.example.h_bankpro.data

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(
    val userId: String,
    val isManager: Boolean,
    val fcmToken: String
)