package com.example.h_bank.presentation.paymentHistory

import com.example.h_bank.data.Account
import com.example.h_bank.data.Payment
import com.example.h_bank.data.PaymentType
import com.example.h_bank.data.PaymentTypeFilter
import java.time.LocalDate
import java.time.LocalDateTime

data class PaymentHistoryState(
    val allPayments: List<Payment> = emptyList(),
    val filteredPayments: List<Payment> = listOf(
        Payment(
            "1",
            PaymentType.INCOMING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("1", "Счёт 1", 100000.toDouble(), "1", false, LocalDateTime.now()),
            null
        )
    ),
    val accounts: List<Account> = emptyList(),
    val selectedAccount: Account? = null,
    val selectedType: PaymentTypeFilter = PaymentTypeFilter.All,
    val selectedDateRange: Pair<LocalDate?, LocalDate?> = null to null,
    val isAccountsSheetVisible: Boolean = false,
    val isTypesSheetVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false
)