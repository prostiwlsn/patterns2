package com.example.h_bankpro.data

import com.example.h_bankpro.data.dto.CurrencyDto
import kotlinx.datetime.LocalDateTime

data class Account(
    val id: String,
    val accountNumber: String,
    val balance: Float,
    val currency: CurrencyDto,
    val userId: String,
    val isDeleted: Boolean,
    val createDateTime: LocalDateTime
)