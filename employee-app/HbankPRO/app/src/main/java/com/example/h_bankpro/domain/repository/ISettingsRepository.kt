package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.UserSettings
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    suspend fun getSettings(): UserSettings
    suspend fun saveSettings(settings: UserSettings)
    val settingsFlow: Flow<UserSettings>
}