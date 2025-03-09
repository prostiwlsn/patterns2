package com.example.h_bank.presentation.paymentHistory.utils

import com.example.h_bank.data.Account
import com.example.h_bank.domain.entity.payment.PaymentItemEntity
import com.example.h_bank.domain.entity.filter.OperationType
import java.time.LocalDate
import java.time.LocalDateTime

fun getAllPayments() = listOf(
    PaymentItemEntity(
        "1",
        OperationType.REPLENISHMENT,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.REPLENISHMENT,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    ),
)

fun getFilteredPayments() = listOf(
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
        null
    )
)

fun getAccounts() = listOf(
    Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
    Account("1", "Счёт 2", 100000.toFloat(), "1", false, LocalDateTime.now()),
    Account("1", "Счёт 3", 100000.toFloat(), "1", false, LocalDateTime.now()),
    Account("1", "Счёт 4", 100000.toFloat(), "1", false, LocalDateTime.now()),
    Account("1", "Счёт 5", 100000.toFloat(), "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toFloat(), "1", false, LocalDateTime.now()),
)