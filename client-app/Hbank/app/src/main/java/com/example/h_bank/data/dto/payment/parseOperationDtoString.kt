package com.example.h_bank.data.dto.payment

import kotlinx.datetime.Instant

object OperationParser {
    fun parseOperationDtoString(text: String): OperationShortDto? {
        return try {
            val regex =
                """OperationDTO\(id=([^,]+),\s*senderAccountId=[^,]+,\s*senderAccountNumber=[^,]+,\s*recipientAccountId=[^,]+,\s*recipientAccountNumber=[^,]+,\s*directionToMe=([^,]+),\s*amount=([^,]+),\s*transactionDateTime=([^,]+),\s*message=[^,]+,\s*operationType=([^,)]+)(?:,\s*isPaymentExpired=[^)]+)?\)""".toRegex()
            regex.matchEntire(text)?.let { match ->
                val (id, directionToMe, amount, transactionDateTime, operationType) = match.destructured
                OperationShortDto(
                    id = id.trim(),
                    amount = amount.trim().toFloat(),
                    directionToMe = directionToMe.trim().toBoolean(),
                    transactionDateTime = Instant.parse(transactionDateTime.trim()),
                    operationType = OperationTypeDto.valueOf(operationType.trim().uppercase())
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}