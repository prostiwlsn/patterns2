package com.example.h_bankpro.presentation.account

import androidx.paging.PagingData
import com.example.h_bankpro.data.OperationTypeFilter
import com.example.h_bankpro.domain.model.OperationShort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

data class AccountState(
    val operations: Flow<PagingData<OperationShort>> = MutableStateFlow(PagingData.empty()),
    val realtimeOperations: List<OperationShort> = emptyList(),
    val selectedOperationType: OperationTypeFilter = OperationTypeFilter.All,
    val isLoading: Boolean = false,
    val accountId: String = "",
    val accountNumber: String = "",
    val currency: String = "",
    val selectedDateRange: Pair<LocalDate?, LocalDate?> = null to null,
    val isTypesSheetVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false
)