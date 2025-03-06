package com.example.h_bankpro.presentation.transactionInfo

import com.example.h_bankpro.domain.model.Operation

data class TransactionInfoState(
    val operationId: String = "",
    val operation: Operation? = null
)
