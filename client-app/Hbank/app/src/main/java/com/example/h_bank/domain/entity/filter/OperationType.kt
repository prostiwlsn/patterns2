package com.example.h_bank.domain.entity.filter

enum class OperationType {
    ALL,
    REPLENISHMENT,
    WITHDRAWAL,
    TRANSFER,
    LOAN_PAYMENT,

    ;

    companion object {
        fun OperationType.getText() = when(this) {
            ALL -> "Все типы"
            REPLENISHMENT -> "Пополнение"
            WITHDRAWAL -> "Снятие"
            TRANSFER -> "Перевод"
            LOAN_PAYMENT -> "Оплата кредита"
        }

        fun OperationType.getQuery() = when(this) {
            ALL -> null
            REPLENISHMENT -> "replenishment"
            WITHDRAWAL -> "withdrawal"
            TRANSFER -> "transfer"
            LOAN_PAYMENT -> "loan_payment"
        }
    }
}