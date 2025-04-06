package com.example.h_bank.presentation.paymentHistory.utils

import com.example.h_bank.data.Account
import com.example.h_bank.data.dto.CurrencyDto
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
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.REPLENISHMENT,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    ),
)

fun getFilteredPayments() = listOf(
    PaymentItemEntity(
        "1",
        OperationType.WITHDRAWAL,
        LocalDate.of(2025, 2, 1),
        500.0,
        Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
        null
    )
)

fun getAccounts() = listOf(
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
    Account("1", "Счёт 1", 100000.toDouble(), CurrencyDto.RUB, "1", false, LocalDateTime.now()),
)