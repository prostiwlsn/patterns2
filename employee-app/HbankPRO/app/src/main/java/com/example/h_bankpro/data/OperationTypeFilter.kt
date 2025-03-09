package com.example.h_bankpro.data

sealed class OperationTypeFilter(val displayName: String) {
    data object All : OperationTypeFilter("Все")
    data class Specific(val type: OperationType) : OperationTypeFilter(type.displayName)
}