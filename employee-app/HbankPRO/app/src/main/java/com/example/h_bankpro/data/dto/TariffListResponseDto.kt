package com.example.h_bankpro.data.dto

import com.example.h_bankpro.domain.model.LoansPageResponse
import com.example.h_bankpro.domain.model.Tariff
import kotlinx.serialization.Serializable

@Serializable
data class TariffListResponseDto(
    val tariffs: List<TariffDto>,
    val pagination: PaginationDto
)

internal fun TariffListResponseDto.toDomain(): LoansPageResponse<Tariff> {
    return LoansPageResponse(
        items = tariffs.map { it.toDomain() },
        pageSize = pagination.pageSize,
        pageNumber = pagination.pageNumber,
        pagesCount = pagination.pagesCount
    )
}