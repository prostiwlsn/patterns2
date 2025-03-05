package com.example.h_bankpro.data

import kotlinx.datetime.LocalDateTime

data class Account(
    val id: String,
    val balance: Float,
    val userId: String,
    val isDeleted: Boolean,
    val createDateTime: LocalDateTime
)