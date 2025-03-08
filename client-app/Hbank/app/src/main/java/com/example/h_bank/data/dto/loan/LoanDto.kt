package com.example.h_bank.data.dto.loan

import com.example.h_bank.data.Loan
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class LoanDto(
    val id: String,
    val documentNumber: Int,
    val amount: Double,
    val debt: Double,
    val ratePercent: Double,
    val issueDate: Instant,
    val endDate: Instant,
)

/*
internal fun LoanDto.toDomain(): Loan {

}*/
