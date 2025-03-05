package com.example.h_bankpro.domain.model

import com.example.h_bankpro.data.RoleType
import java.util.UUID

data class User(
    val id: String,
    val phone: String,
    val name: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>
)


