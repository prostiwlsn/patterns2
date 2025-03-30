package com.example.h_bankpro.data.dto

import com.example.h_bankpro.data.ThemeMode
import com.example.h_bankpro.data.UserSettings
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SettingsResponse(
    @SerialName("theme") val theme: String,
)

internal fun SettingsResponse.toDomain() = UserSettings(
    theme = ThemeMode.valueOf(theme.uppercase())
)