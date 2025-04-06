package com.example.h_bank.domain.repository.settings

import com.example.h_bank.data.UserSettings
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    suspend fun getSettings(): UserSettings
    suspend fun saveSettings(settings: UserSettings)
    val settingsFlow: Flow<UserSettings>
}