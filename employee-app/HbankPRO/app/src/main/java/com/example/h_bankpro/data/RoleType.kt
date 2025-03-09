package com.example.h_bankpro.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RoleType(val displayName: String) {
    @SerialName("Manager")
    MANAGER("Менеджер"),

    @SerialName("Admin")
    ADMIN("Администратор");
}
