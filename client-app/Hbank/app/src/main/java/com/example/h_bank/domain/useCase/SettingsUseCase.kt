package com.example.h_bank.domain.useCase

import com.example.h_bank.data.UserSettings
import com.example.h_bank.data.ThemeMode
import com.example.h_bank.data.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsUseCase(private val settingsRepository: SettingsRepository) {
    val settingsFlow: Flow<UserSettings> = settingsRepository.settingsFlow

    suspend fun setTheme(theme: ThemeMode) {
        val current = settingsRepository.getSettings()
        settingsRepository.saveSettings(current.copy(theme = theme))
    }

    suspend fun toggleAccountVisibility(accountId: String) {
        val current = settingsRepository.getSettings()
        val hidden = current.hiddenAccounts.toMutableSet()
        if (hidden.contains(accountId)) hidden.remove(accountId) else hidden.add(accountId)
        settingsRepository.saveSettings(current.copy(hiddenAccounts = hidden))
    }

    suspend fun removeAccountFromHidden(accountId: String) {
        val current = settingsRepository.getSettings()
        val hidden = current.hiddenAccounts.toMutableSet()
        if (hidden.contains(accountId)) {
            hidden.remove(accountId)
            settingsRepository.saveSettings(current.copy(hiddenAccounts = hidden))
        }
    }

    suspend fun getSettings(): UserSettings = settingsRepository.getSettings()
}