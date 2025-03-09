package com.example.h_bank.data.dto.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OperationTypeDto(val displayName: String) {
    @SerialName("replenishment")
    REPLENISHMENT("Пополнение"),

    @SerialName("withdrawal")
    WITHDRAWAL("Снятие"),

    @SerialName("transfer")
    TRANSFER("Перевод"),

    @SerialName("loan_payment")
    LOAN_REPAYMENT("Оплата кредита");
}