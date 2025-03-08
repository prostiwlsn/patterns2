package com.example.h_bankpro.data.dto

import com.example.h_bankpro.domain.model.Loan
import kotlinx.serialization.Serializable

@Serializable
data class LoanListResponseDto(
    val loans: List<LoanDto>,
    val pagination: PaginationDto
)

internal fun LoanListResponseDto.toDomainList(): List<Loan> {
    return loans.map { it.toDomain() }
}