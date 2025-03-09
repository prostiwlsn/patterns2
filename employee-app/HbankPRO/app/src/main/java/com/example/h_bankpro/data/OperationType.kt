package com.example.h_bankpro.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OperationType(val displayName: String) {
    @SerialName("replenishment")
    REPLENISHMENT("Пополнение"),

    @SerialName("withdrawal")
    WITHDRAWAL("Снятие"),

    @SerialName("transfer")
    TRANSFER("Перевод"),

    @SerialName("loan_payment")
    LOAN_REPAYMENT("Оплата кредита");
}