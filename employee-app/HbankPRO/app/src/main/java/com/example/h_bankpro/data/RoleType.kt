package com.example.h_bankpro.data

enum class RoleType(val displayName: String) {
    MANAGER("Менеджер"),
    ADMIN("Администратор");

    companion object {
        fun fromString(value: String): RoleType? {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}
