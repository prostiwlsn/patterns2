package com.example.h_bank.data

enum class PaymentType(val displayName: String) {
    INCOMING("Входящий платеж"),
    OUTGOING("Исходящий платеж");

    companion object {
        fun fromDisplayName(name: String): PaymentType =
            entries.find { it.displayName == name } ?: INCOMING
    }
}