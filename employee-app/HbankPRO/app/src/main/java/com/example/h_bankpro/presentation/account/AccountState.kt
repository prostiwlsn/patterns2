package com.example.h_bankpro.presentation.account

import com.example.h_bankpro.data.Account
import com.example.h_bankpro.data.Payment
import com.example.h_bankpro.data.PaymentType
import com.example.h_bankpro.data.PaymentTypeFilter
import java.time.LocalDate

data class AccountState(
    val allPayments: List<Payment> = listOf(
//        Payment(
//            "1",
//            PaymentType.INCOMING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("1", "Счёт 1", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
//        Payment(
//            "1",
//            PaymentType.OUTGOING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("2", "Счёт 2", 100000),
//            null
//        ),
    ),
    val filteredPayments: List<Payment> = listOf(
//        Payment(
//            "1",
//            PaymentType.INCOMING,
//            LocalDate.of(2025, 2, 1),
//            500.0,
//            Account("1", "Счёт 1", 100000),
//            null
//        )
    ),
    val accountId: String = "Счёт · 7928",
    val selectedType: PaymentTypeFilter = PaymentTypeFilter.All,
    val selectedDateRange: Pair<LocalDate?, LocalDate?> = null to null,
    val isTypesSheetVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false
)