package com.example.h_bankpro.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tariff(
    val id: String,
    val name: String,
    val ratePercent: Double,
    val description: String
)