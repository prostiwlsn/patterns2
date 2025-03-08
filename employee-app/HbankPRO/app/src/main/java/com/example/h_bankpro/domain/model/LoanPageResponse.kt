package com.example.h_bankpro.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoansPageResponse<T>(
    val items: List<T>,
    val pageSize: Int,
    val pageNumber: Int,
    val pagesCount: Int
)