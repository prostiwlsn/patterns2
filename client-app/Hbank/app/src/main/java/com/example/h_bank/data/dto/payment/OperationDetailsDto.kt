package com.example.h_bank.data.dto.payment

import kotlinx.serialization.Serializable

@Serializable
class OperationDetailsDto(
    val id: String,
    val senderAccountId: String?,
    val senderAccountNumber: String?,
    val recipientAccountId: String?,
    val recipientAccountNumber: String?,
    val directionToMe: Boolean?,
    val amount: Double,
    val transactionDateTime: String,
    val message: String?,
    val operationType: OperationTypeDto,
)