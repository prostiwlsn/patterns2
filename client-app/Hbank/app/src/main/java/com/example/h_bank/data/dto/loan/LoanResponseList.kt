package com.example.h_bank.data.dto.loan

import com.example.h_bank.data.dto.common.PaginationDto
import kotlinx.serialization.Serializable

@Serializable
data class LoanListResponseDto(
    val loans: List<LoanDto>,
    val pagination: PaginationDto,
)

/*
internal fun LoanListResponseDto.toDomain(): LoansPageResponse<Loan> {
    return LoansPageResponse(
        items = loans.map { it.toDomain() },
        pageSize = pagination.pageSize,
        pageNumber = pagination.pageNumber,
        pagesCount = pagination.pagesCount
    )
}*/
