package com.example.h_bank.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    val pageSize: Int,
    val pageNumber: Int,
    val pagesCount: Int,
)