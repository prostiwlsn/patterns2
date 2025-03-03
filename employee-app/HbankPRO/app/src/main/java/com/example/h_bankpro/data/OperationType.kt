package com.example.h_bankpro.data

enum class OperationType(val displayName: String) {
    REPLENISHMENT("Пополнение"),
    WITHDRAWAL("Снятие"),
    TRANSFER("Перевод"),
    LOAN_REPAYMENT("Оплата кредита");
}