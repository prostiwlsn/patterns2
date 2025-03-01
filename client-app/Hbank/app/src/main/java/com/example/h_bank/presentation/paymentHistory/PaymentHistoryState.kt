package com.example.h_bank.presentation.paymentHistory

import com.example.h_bank.data.Account
import com.example.h_bank.data.Payment
import com.example.h_bank.data.PaymentType
import com.example.h_bank.data.PaymentTypeFilter
import java.time.LocalDate

data class PaymentHistoryState(
    val allPayments: List<Payment> = listOf(
        Payment(
            "1",
            PaymentType.INCOMING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("1", "Счёт 1", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
        Payment(
            "1",
            PaymentType.OUTGOING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("2", "Счёт 2", "100000")
        ),
    ),
    val filteredPayments: List<Payment> = listOf(
        Payment(
            "1",
            PaymentType.INCOMING,
            LocalDate.of(2025, 2, 1),
            500.0,
            Account("1", "Счёт 1", "100000")
        )
    ),
    val accounts: List<Account> = listOf(
        Account("1", "Счёт 1", "100000"),
        Account("2", "Счёт 2", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
        Account("1", "Счёт 1", "100000"),
    ),
    val selectedAccount: Account? = null,
    val selectedType: PaymentTypeFilter = PaymentTypeFilter.All,
    val selectedDateRange: Pair<LocalDate?, LocalDate?> = null to null,
    val isAccountsSheetVisible: Boolean = false,
    val isTypesSheetVisible: Boolean = false,
    val isDatePickerVisible: Boolean = false
)