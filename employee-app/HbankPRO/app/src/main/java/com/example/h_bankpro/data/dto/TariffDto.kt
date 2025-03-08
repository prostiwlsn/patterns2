package com.example.h_bankpro.data.dto

import com.example.h_bankpro.domain.model.Tariff
import kotlinx.serialization.Serializable

@Serializable
data class TariffDto(
    val id: String,
    val name: String,
    val ratePercent: Double,
    val description: String
)

internal fun TariffDto.toDomain(): Tariff {
    return Tariff(
        id = id,
        name = name,
        ratePercent = ratePercent,
        description = description
    )
}