package com.example.h_bankpro.data

import java.util.UUID

data class Rate(
    val id: UUID,
    val name: String,
    val interestRate: Float,
    val description: String
)