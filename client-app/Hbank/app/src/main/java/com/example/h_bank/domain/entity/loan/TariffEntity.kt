package com.example.h_bank.domain.entity.loan

import kotlinx.serialization.Serializable

@Serializable
data class TariffEntity(
    val id: String,
    val name: String,
    val ratePercent: Double,
    val description: String
)