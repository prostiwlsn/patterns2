package com.example.h_bank.domain.entity.filter

import java.time.LocalDateTime

data class OperationFilterEntity(
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val accountId: String? = null,
    val operationType: OperationType? = null,
)