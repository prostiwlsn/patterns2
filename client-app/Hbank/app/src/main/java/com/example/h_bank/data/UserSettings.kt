package com.example.h_bank.data

data class UserSettings(
    val theme: ThemeMode = ThemeMode.LIGHT,
    val hiddenAccounts: Set<String> = emptySet()
)