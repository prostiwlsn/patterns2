package com.example.h_bank.presentation.paymentHistory.model

import com.example.h_bank.domain.entity.filter.AccountFilter
import com.example.h_bank.domain.entity.filter.FilterItem
import com.example.h_bank.domain.entity.filter.OperationTypeFilter
import com.example.h_bank.domain.entity.filter.PeriodFilter
import com.example.h_bank.domain.useCase.filter.UpdateOperationsFilterUseCase

data class OperationsFilterModel(
    var accountFilter: AccountFilter = AccountFilter(text = "Нет счетов"),
    var periodFilter: PeriodFilter = PeriodFilter(),
    var typeFilter: OperationTypeFilter = OperationTypeFilter(),
) {
    private val initialAccountFilter = accountFilter

    fun getFiltersList() = listOf(
        accountFilter,
        periodFilter,
        typeFilter,
    )

    fun isFilterSelected() = getFiltersList().any { it.isChecked() && it.isCloseable() }

    fun updateFilter(filter: FilterItem): OperationsFilterModel {
        when (filter) {
            is AccountFilter -> accountFilter = filter
            is PeriodFilter -> periodFilter = filter
            is OperationTypeFilter -> typeFilter = filter
        }

        return this
    }

    fun resetFilter(filter: FilterItem): OperationsFilterModel {
        when (filter) {
            is AccountFilter -> accountFilter = initialAccountFilter
            is PeriodFilter -> periodFilter = PeriodFilter()
            is OperationTypeFilter -> typeFilter = OperationTypeFilter()
        }

        return this
    }
}