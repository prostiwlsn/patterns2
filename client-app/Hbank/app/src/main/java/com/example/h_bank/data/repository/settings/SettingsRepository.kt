package com.example.h_bank.data.repository.settings

import com.example.h_bank.data.ThemeMode
import com.example.h_bank.data.UserSettings
import com.example.h_bank.data.dto.SettingsRequest
import com.example.h_bank.data.dto.toDomain
import com.example.h_bank.data.dataSource.local.SettingsLocalDataSource
import com.example.h_bank.data.dataSource.remote.SettingsRemoteDataSource
import com.example.h_bank.domain.repository.settings.ISettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val localDataSource: SettingsLocalDataSource,
    private val remoteDataSource: SettingsRemoteDataSource
) : ISettingsRepository {
    override suspend fun getSettings(): UserSettings {
        println("SettingsRepository: Attempting to fetch settings from server")
        try {
            val serverSettings = remoteDataSource.getSettings().toDomain()
            println("SettingsRepository: Loaded from server = $serverSettings")
            localDataSource.saveSettings(serverSettings)
            return serverSettings
        } catch (e: Exception) {
            println("SettingsRepository: Server fetch failed: $e")
            val localSettings = localDataSource.getSettings()
            println("SettingsRepository: Loaded from local = $localSettings")
            return if (localSettings != null) {
                localSettings
            } else {
                val defaultSettings = UserSettings(theme = ThemeMode.LIGHT)
                println("SettingsRepository: Using default = $defaultSettings")
                localDataSource.saveSettings(defaultSettings)
                defaultSettings
            }
        }
    }

    override suspend fun saveSettings(settings: UserSettings) {
        localDataSource.saveSettings(settings)
        println("SettingsRepository: Saved settings locally = $settings")
        try {
            remoteDataSource.saveSettings(settings.toRequest())
            println("SettingsRepository: Saved settings to server = $settings")
        } catch (e: Exception) {
            println("SettingsRepository: Failed to save settings to server: $e")
        }
    }

    override val settingsFlow: Flow<UserSettings> = localDataSource.settingsFlow

    private fun UserSettings.toRequest() = SettingsRequest(theme.name, hiddenAccounts.toList())
}