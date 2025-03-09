package com.example.h_bank.presentation.paymentHistory

import androidx.paging.PagingData
import com.example.h_bank.data.Account
import com.example.h_bank.domain.entity.payment.PaymentItemEntity
import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel
import com.example.h_bank.presentation.paymentHistory.model.OperationsFilterModel
import com.example.h_bank.presentation.paymentHistory.utils.getAccounts
import com.example.h_bank.presentation.paymentHistory.utils.getAllPayments
import com.example.h_bank.presentation.paymentHistory.utils.getFilteredPayments
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

data class PaymentHistoryState(
    val allPayments: List<PaymentItemEntity> = getAllPayments(),
    val filteredPayments: List<PaymentItemEntity> = getFilteredPayments(),
    val accounts: List<Account> = getAccounts(),
    val isAccountsSheetVisible: Boolean = false,
    val isTypesSheetVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false,
    val operationsPager: Flow<PagingData<OperationShortModel>> = MutableStateFlow(PagingData.empty()),
    val filterModel: OperationsFilterModel = OperationsFilterModel(),
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
)