package com.example.h_bank.data

import kotlinx.datetime.LocalDateTime

data class Account(
    val id: String,
    val accountNumber: String,
    val balance: Float,
    val userId: String,
    val isDeleted: Boolean,
    val createDateTime: LocalDateTime
)