package com.example.h_bankpro.data.dto

import com.example.h_bankpro.data.Account
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant
import com.example.h_bankpro.data.utils.InstantSerializer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Serializable
data class AccountDTO(
    val id: String,
    val accountNumber: String,
    val balance: Float,
    val userId: String,
    val isDeleted: Boolean,
    @Serializable(with = InstantSerializer::class)
    val createDateTime: Instant
)

internal fun AccountDTO.toDomain(): Account {
    return Account(
        id = id,
        accountNumber = accountNumber,
        balance = balance,
        userId = userId,
        isDeleted = isDeleted,
        createDateTime = createDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
    )
}