package com.example.h_bank.data.dto

import com.example.h_bank.data.Account
import com.example.h_bank.data.utils.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AccountDto(
    val id: String,
    val accountNumber: String,
    val balance: Float,
    val userId: String,
    val isDeleted: Boolean,
    @Serializable(with = InstantSerializer::class)
    val createDateTime: Instant
)

internal fun AccountDto.toDomain(): Account {
    return Account(
        id = id,
        accountNumber = accountNumber,
        balance = balance,
        userId = userId,
        isDeleted = isDeleted,
        createDateTime = LocalDateTime.now()
    )
}