package com.example.h_bank.data

import java.time.LocalDateTime

data class Account(
    val id: String,
    val accountNumber: String,
    val balance: Double,
    val userId: String,
    val isDeleted: Boolean,
    val createDateTime: LocalDateTime
)