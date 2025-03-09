package com.example.h_bank.domain.entity.loan

import kotlinx.serialization.Serializable

@Serializable
data class LoansPageResponse<T>(
    val items: List<T>,
    val pageSize: Int,
    val pageNumber: Int,
    val pagesCount: Int
)