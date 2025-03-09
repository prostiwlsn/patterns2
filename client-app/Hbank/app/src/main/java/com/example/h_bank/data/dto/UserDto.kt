package com.example.h_bank.data.dto

import com.example.h_bank.data.RoleType
import com.example.h_bank.domain.entity.authorization.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val phone: String,
    val name: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>
)

internal fun UserDto.toDomain(): UserEntity {
    return UserEntity(
        id = id,
        phone = phone,
        name = name,
        isBlocked = isBlocked,
        roles = roles
    )
}