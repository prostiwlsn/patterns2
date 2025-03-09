package com.example.h_bank.data.dto

import com.example.h_bank.data.Account
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
data class AccountDto(
    val id: String,
    val accountNumber: String,
    val balance: Double,
    val userId: String,
    val isDeleted: Boolean,
    val createDateTime: Instant
)

internal fun AccountDto.toDomain(): Account {
    return Account(
        id = id,
        accountNumber = accountNumber,
        balance = balance,
        userId = userId,
        isDeleted = isDeleted,
        createDateTime = createDateTime.toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    )
}