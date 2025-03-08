package com.example.h_bank.data.dto.loan

import com.example.h_bank.domain.entity.loan.TariffEntity
import kotlinx.serialization.Serializable

@Serializable
data class TariffDto(
    val id: String,
    val name: String,
    val ratePercent: Double,
    val description: String
)

internal fun TariffDto.toDomain(): TariffEntity {
    return TariffEntity(
        id = id,
        name = name,
        ratePercent = ratePercent,
        description = description
    )
}
