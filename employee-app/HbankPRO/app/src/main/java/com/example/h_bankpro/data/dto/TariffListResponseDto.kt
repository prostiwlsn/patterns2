package com.example.h_bankpro.data.dto

import com.example.h_bankpro.domain.model.Tariff
import kotlinx.serialization.Serializable

@Serializable
data class TariffListResponseDto(
    val tariffs: List<TariffDto>,
    val pagination: PaginationDto
)

internal fun TariffListResponseDto.toDomainList(): List<Tariff> {
    return tariffs.map { it.toDomain() }
}