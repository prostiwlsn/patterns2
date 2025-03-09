package com.example.h_bank.domain.entity.authorization

import com.example.h_bank.data.RoleType

data class UserEntity(
    val id: String,
    val phone: String,
    val name: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>
)