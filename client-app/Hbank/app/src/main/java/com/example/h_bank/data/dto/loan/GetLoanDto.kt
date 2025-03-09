package com.example.h_bank.data.dto.loan

import kotlinx.serialization.Serializable

@Serializable
class GetLoanDto(
    val tariffId: String,
    val userId: String,
    val accountId: String,
    val durationInYears: Int,
    val amount: Int,
)