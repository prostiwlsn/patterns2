package com.example.h_bank.domain.entity.filter

import com.example.h_bank.data.dto.payment.OperationTypeDto


enum class OperationType {
    ALL,
    REPLENISHMENT,
    WITHDRAWAL,
    TRANSFER,
    LOAN_REPAYMENT,

    ;

    companion object {
        fun OperationType.getText() = when(this) {
            ALL -> "Все типы"
            REPLENISHMENT -> "Пополнение"
            WITHDRAWAL -> "Снятие"
            TRANSFER -> "Перевод"
            LOAN_REPAYMENT -> "Оплата кредита"
        }

        fun OperationType.getQuery() = when(this) {
            ALL -> null
            REPLENISHMENT -> "replenishment"
            WITHDRAWAL -> "withdrawal"
            TRANSFER -> "transfer"
            LOAN_REPAYMENT -> "loan_payment"
        }
    }
}

fun OperationTypeDto.toDomain() = when(this) {
    OperationTypeDto.TRANSFER -> OperationType.TRANSFER
    OperationTypeDto.WITHDRAWAL -> OperationType.WITHDRAWAL
    OperationTypeDto.REPLENISHMENT -> OperationType.REPLENISHMENT
    OperationTypeDto.LOAN_REPAYMENT -> OperationType.LOAN_REPAYMENT
}