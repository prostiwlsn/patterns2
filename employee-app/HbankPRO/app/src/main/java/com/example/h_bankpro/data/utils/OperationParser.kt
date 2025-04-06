package com.example.h_bankpro.data.utils

import com.example.h_bankpro.data.OperationType
import com.example.h_bankpro.data.dto.OperationShortDto
import kotlinx.datetime.Instant

object OperationParser {
    fun parseOperationDtoString(text: String): OperationShortDto? {
        return try {
            val regex =
                """OperationDTO\(id=([^,]+),\s*accountId=[^,]+,\s*directionToMe=([^,]+),\s*amount=([^,]+),\s*transactionDateTime=([^,]+),\s*operationType=([^,)]+)\)""".toRegex()
            regex.matchEntire(text)?.let { match ->
                val (id, directionToMe, amount, transactionDateTime, operationType) = match.destructured
                OperationShortDto(
                    id = id.trim(),
                    amount = amount.trim().toFloat(),
                    directionToMe = directionToMe.trim().toBoolean(),
                    transactionDateTime = Instant.parse(transactionDateTime.trim()),
                    operationType = OperationType.valueOf(operationType.trim().uppercase())
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}