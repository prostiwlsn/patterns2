package com.example.h_bankpro.data.dto

import com.example.h_bankpro.domain.model.Loan
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
data class LoanDto(
    val id: String,
    val documentNumber: Int,
    val amount: Double,
    val debt: Double,
    val ratePercent: Double,
    val isExpired: Boolean,
    val issueDate: Instant,
    val endDate: Instant,
)

internal fun LoanDto.toDomain(): Loan {
    return Loan(
        id = id,
        documentNumber = documentNumber,
        amount = amount,
        debt = debt,
        ratePercent = ratePercent,
        isExpired = isExpired,
        issueDate = issueDate.toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
        endDate = endDate.toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    )
}