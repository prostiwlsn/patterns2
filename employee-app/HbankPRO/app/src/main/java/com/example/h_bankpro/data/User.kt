package com.example.h_bankpro.data

import java.util.UUID

data class User(
    val id: UUID,
    val phone: String,
    val name: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>
)
