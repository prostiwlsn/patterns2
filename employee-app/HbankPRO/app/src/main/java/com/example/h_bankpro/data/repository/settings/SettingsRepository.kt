package com.example.h_bankpro.data.repository.settings

import com.example.h_bankpro.data.ThemeMode
import com.example.h_bankpro.data.UserSettings
import com.example.h_bankpro.data.dataSource.local.SettingsLocalDataSource
import com.example.h_bankpro.data.dataSource.remote.SettingsRemoteDataSource
import com.example.h_bankpro.data.dto.SettingsRequest
import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.domain.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val localDataSource: SettingsLocalDataSource,
    private val remoteDataSource: SettingsRemoteDataSource
) : ISettingsRepository {
    override suspend fun getSettings(): UserSettings {
        try {
            val serverSettings = remoteDataSource.getSettings().toDomain()
            localDataSource.saveSettings(serverSettings)
            return serverSettings
        } catch (e: Exception) {
            val localSettings = localDataSource.getSettings()
            return if (localSettings != null) {
                localSettings
            } else {
                val defaultSettings = UserSettings(theme = ThemeMode.LIGHT)
                localDataSource.saveSettings(defaultSettings)
                defaultSettings
            }
        }
    }

    override suspend fun saveSettings(settings: UserSettings) {
        localDataSource.saveSettings(settings)
        try {
            remoteDataSource.saveSettings(settings.toRequest())
        } catch (e: Exception) {
        }
    }

    override val settingsFlow: Flow<UserSettings> = localDataSource.settingsFlow

    private fun UserSettings.toRequest() = SettingsRequest(theme.name)
}