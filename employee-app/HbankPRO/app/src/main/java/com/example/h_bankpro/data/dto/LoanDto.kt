package com.example.h_bankpro.data.dto

import com.example.h_bankpro.domain.model.Loan
import kotlinx.serialization.Serializable

@Serializable
data class LoanDto(
    val id: String,
    val userId: String,
    val tariffId: String,
    val amount: Double,
    val status: String
)

internal fun LoanDto.toDomain(): Loan {
    return Loan(
        id = id,
        userId = userId,
        tariffId = tariffId,
        amount = amount,
        status = status
    )
}