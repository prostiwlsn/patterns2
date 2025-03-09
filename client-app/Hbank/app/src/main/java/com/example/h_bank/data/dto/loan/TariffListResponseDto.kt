package com.example.h_bank.data.dto.loan

import com.example.h_bank.data.dto.common.PaginationDto
import com.example.h_bank.domain.entity.loan.LoansPageResponse
import com.example.h_bank.domain.entity.loan.TariffEntity
import kotlinx.serialization.Serializable

@Serializable
data class TariffListResponseDto(
    val tariffs: List<TariffDto>,
    val pagination: PaginationDto
)

internal fun TariffListResponseDto.toDomain(): LoansPageResponse<TariffEntity> {
    return LoansPageResponse(
        items = tariffs.map { it.toDomain() },
        pageSize = pagination.pageSize,
        pageNumber = pagination.pageNumber,
        pagesCount = pagination.pagesCount
    )
}
