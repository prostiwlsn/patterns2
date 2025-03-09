package com.example.h_bank.presentation.paymentHistory.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object DateFormatter {
    fun LocalDateTime.toStringFormat(): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return this.format(formatter)
    }
}