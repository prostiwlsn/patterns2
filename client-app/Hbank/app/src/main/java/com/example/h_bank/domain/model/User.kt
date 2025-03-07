package com.example.h_bank.domain.model

import com.example.h_bank.data.RoleType

data class User(
    val id: String,
    val phone: String,
    val name: String,
    val isBlocked: Boolean,
    val roles: List<RoleType>
)