package com.example.h_bank.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Pageable(
    val page: Int = 0,
    val size: Int = 20,
    val sort: List<String> = listOf("transactionDateTime,desc")
)