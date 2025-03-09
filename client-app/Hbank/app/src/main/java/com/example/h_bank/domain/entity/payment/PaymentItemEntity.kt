package com.example.h_bank.domain.entity.payment

import com.example.h_bank.data.Account
import com.example.h_bank.domain.entity.filter.OperationType
import java.time.LocalDate

data class PaymentItemEntity(
    val id: String,
    val type: OperationType,
    val date: LocalDate,
    val amount: Double,
    val account: Account,
    val comment: String?
)