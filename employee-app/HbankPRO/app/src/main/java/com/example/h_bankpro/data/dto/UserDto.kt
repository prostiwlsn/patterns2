package com.example.h_bankpro.data.dto

import com.example.h_bankpro.data.RoleType
import com.example.h_bankpro.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val phone: String,
    val name: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>
)

internal fun UserDto.toDomain(): User {
    return User(
        id = id,
        phone = phone,
        name = name,
        isBlocked = isBlocked,
        roles = roles
    )
}