package com.example.h_bank.data

sealed class PaymentTypeFilter(val displayName: String) {
    data object All : PaymentTypeFilter("Все типы")
    data class Specific(val type: PaymentType) : PaymentTypeFilter(type.displayName)
}