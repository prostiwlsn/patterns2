package com.example.h_bankpro.presentation.operationInfo

import com.example.h_bankpro.domain.model.Operation

data class OperationInfoState(
    val operation: Operation? = null,
    val displayTitle: String = "",
    val formattedAmount: String = "",
    val formattedDateTime: String = "",
    val isLoading: Boolean = false
)
