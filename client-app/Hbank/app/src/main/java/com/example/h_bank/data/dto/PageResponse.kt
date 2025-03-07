package com.example.h_bank.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val first: Boolean,
    val last: Boolean,
    val size: Int,
    val number: Int,
    val sort: SortSummary,
    val pageable: PageableObject,
    val numberOfElements: Int,
    val empty: Boolean
)

@Serializable
data class SortSummary(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

@Serializable
data class PageableObject(
    val offset: Long,
    val sort: SortSummary,
    val paged: Boolean,
    val pageNumber: Int,
    val pageSize: Int,
    val unpaged: Boolean
)