package com.example.h_bank.data.dto.loan

import com.example.h_bank.data.Loan
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
    val issueDate: String,
    val endDate: String,
)

internal fun LoanDto.toDomain(): Loan {
    return try {
        val issueInstant = Instant.parse("${issueDate}Z")
        val endInstant = Instant.parse("${endDate}Z")
        Loan(
            id = id,
            documentNumber = documentNumber,
            amount = amount,
            debt = debt,
            ratePercent = ratePercent,
            isExpired = isExpired,
            issueDate = issueInstant.toJavaInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate(),
            endDate = endInstant.toJavaInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        )
    } catch (e: Exception) {
        throw e
    }
}
