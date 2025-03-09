package com.example.h_bank.data.dto

import com.example.h_bank.data.RoleType
import kotlinx.serialization.Serializable

@Serializable
class UserCreationDto(
    val phone: String,
    val password: String,
    val name: String,
    val role: RoleType?
)