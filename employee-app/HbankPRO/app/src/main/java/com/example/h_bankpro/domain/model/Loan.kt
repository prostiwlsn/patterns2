package com.example.h_bankpro.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Loan(
    val id: String,
    val userId: String,
    val tariffId: String,
    val amount: Double,
    val status: String
)