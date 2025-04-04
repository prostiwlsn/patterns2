package com.example.h_bankpro.presentation.loan

import androidx.paging.PagingData
import com.example.h_bankpro.domain.model.Operation
import com.example.h_bankpro.domain.model.OperationShort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

data class LoanState(
    val expiredPayments: Flow<PagingData<OperationShort>> = MutableStateFlow(PagingData.empty()),
    val operationsPager: Flow<PagingData<Operation>> = MutableStateFlow(PagingData.empty()),
    val isLoading: Boolean = false,
    val documentNumber: String = "",
    val amount: String = "",
    val endDate: String = "",
    val ratePercent: String = "",
    val debt: String = ""
)