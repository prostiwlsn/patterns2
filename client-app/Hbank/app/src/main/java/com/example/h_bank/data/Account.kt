package com.example.h_bank.data

import com.example.h_bank.data.dto.CurrencyDto
import java.time.LocalDateTime

data class Account(
    val id: String,
    val accountNumber: String,
    val balance: Double,
    val currency: CurrencyDto,
    val userId: String,
    val isDeleted: Boolean,
    val createDateTime: LocalDateTime
)