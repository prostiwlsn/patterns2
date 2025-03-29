package com.example.h_bank.data.dto

import com.example.h_bank.data.ThemeMode
import com.example.h_bank.data.UserSettings
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SettingsResponse(
    @SerialName("theme") val theme: String,
    @SerialName("hiddenAccounts") val hiddenAccounts: List<String>
)

internal fun SettingsResponse.toDomain() = UserSettings(
    theme = ThemeMode.valueOf(theme.uppercase()),
    hiddenAccounts = hiddenAccounts.toSet()
)