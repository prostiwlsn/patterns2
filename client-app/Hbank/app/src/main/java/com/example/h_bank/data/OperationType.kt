package com.example.h_bank.data

enum class OperationType(val displayName: String) {
    REPLENISHMENT("replenishment"),
    WITHDRAWAL("withdrawal"),
    TRANSFER("transfer"),
    LOAN_REPAYMENT("loan_payment");
}