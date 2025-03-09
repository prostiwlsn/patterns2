package com.example.h_bank.presentation.transactionInfo

import com.example.h_bank.domain.entity.filter.OperationType

data class TransactionInfoState(
    val senderAccount: String? = null,
    val recipientAccount: String? = null,
    val comment: String? = null,
    val amount: String? = null,
    val date: String? = null,
    val operationType: OperationType? = null,
    val isLoading: Boolean = true,
)