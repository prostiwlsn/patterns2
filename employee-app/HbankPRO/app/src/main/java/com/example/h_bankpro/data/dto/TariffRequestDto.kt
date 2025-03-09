package com.example.h_bankpro.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TariffRequestDto(
    val name: String,
    val ratePercent: Double,
    val description: String
)