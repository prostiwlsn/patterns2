package com.example.h_bank.presentation.paymentHistory.utils

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
// 2025-03-09T06:26:43.755396Z

object DateFormatter {
    fun LocalDateTime.toStringFormat(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss.")
        return this.format(formatter)
    }

    fun String.toLocalDateTime(): String {
        val dateTime = OffsetDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return dateTime.format(formatter)
    }
}