package com.example.h_bank.domain.entity.filter

import com.example.h_bank.domain.entity.filter.OperationType.Companion.getText

sealed interface FilterItem {
    fun isChecked(): Boolean
    fun isCloseable(): Boolean
    fun getText(): String
}

data class AccountFilter(
    private val text: String,
): FilterItem {
    override fun isChecked() = true

    override fun isCloseable() = false

    override fun getText(): String = text
}

data class PeriodFilter(
    private val period: String? = null
): FilterItem {
    override fun isChecked(): Boolean = period != null

    override fun isCloseable() = true

    override fun getText() = period ?: "Период"
}

data class OperationTypeFilter(
    private val type: OperationType = OperationType.ALL
): FilterItem {
    override fun isChecked() = type != OperationType.ALL

    override fun isCloseable() = true

    override fun getText() = type.getText()
}