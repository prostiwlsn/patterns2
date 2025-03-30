package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.ThemeMode
import com.example.h_bankpro.data.UserSettings
import com.example.h_bankpro.data.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsUseCase(private val settingsRepository: SettingsRepository) {
    val settingsFlow: Flow<UserSettings> = settingsRepository.settingsFlow

    suspend fun setTheme(theme: ThemeMode) {
        val current = settingsRepository.getSettings()
        settingsRepository.saveSettings(current.copy(theme = theme))
    }

    suspend fun getSettings(): UserSettings = settingsRepository.getSettings()
}