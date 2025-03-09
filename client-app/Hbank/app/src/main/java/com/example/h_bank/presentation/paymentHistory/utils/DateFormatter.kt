package com.example.h_bank.presentation.paymentHistory.utils

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DateFormatter {
    fun LocalDateTime.toStringFormat(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return this.format(formatter)
    }

    fun String.toLocalDateTime(): String {
        val dateTime = OffsetDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return dateTime.format(formatter)
    }
}