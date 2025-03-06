package com.example.h_bankpro.presentation.account

import com.example.h_bankpro.data.OperationTypeFilter
import com.example.h_bankpro.data.PaymentTypeFilter
import com.example.h_bankpro.domain.model.OperationShort
import java.time.LocalDate

data class AccountState(
    val allOperations: List<OperationShort> = emptyList(),
    val filteredOperations: List<OperationShort> = emptyList(),
    val currentPage: Int = 0,
    val pageSize: Int = 20,
    val totalPages: Int = 1,
    val selectedOperationType: OperationTypeFilter = OperationTypeFilter.All,
    val isLoading: Boolean = false,
    val accountId: String = "",
    val accountNumber: String = "",
    val selectedDateRange: Pair<LocalDate?, LocalDate?> = null to null,
    val isTypesSheetVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false
)