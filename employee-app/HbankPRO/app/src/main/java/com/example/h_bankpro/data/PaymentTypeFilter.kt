package com.example.h_bankpro.data

sealed class PaymentTypeFilter(val displayName: String) {
    data object All : PaymentTypeFilter("Все типы")
    data class Specific(val type: PaymentType) : PaymentTypeFilter(type.displayName)
}