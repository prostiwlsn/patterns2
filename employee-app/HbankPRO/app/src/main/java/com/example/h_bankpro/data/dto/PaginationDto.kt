package com.example.h_bankpro.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    val pageSize: Int,
    val pageNumber: Int,
    val pagesCount: Int
)