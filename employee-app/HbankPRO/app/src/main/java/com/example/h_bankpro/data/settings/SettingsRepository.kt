package com.example.h_bankpro.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.h_bankpro.data.ThemeMode
import com.example.h_bankpro.data.UserSettings
import com.example.h_bankpro.data.dto.SettingsRequest
import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.network.SettingsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface SettingsRepository {
    suspend fun getSettings(): UserSettings
    suspend fun saveSettings(settings: UserSettings)
    val settingsFlow: Flow<UserSettings>
}

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val settingsApi: SettingsApi
) : SettingsRepository {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }

    override suspend fun getSettings(): UserSettings {
        println("SettingsRepository: Attempting to fetch settings from server")
        try {
            val serverSettings = settingsApi.getSettings().toDomain()
            saveLocalSettings(serverSettings)
            return serverSettings
        } catch (e: Exception) {
            val localSettings = dataStore.data.first().toSettings()
            return if (localSettings != null) {
                localSettings
            } else {
                val defaultSettings = UserSettings(theme = ThemeMode.LIGHT)
                saveLocalSettings(defaultSettings)
                defaultSettings
            }
        }
    }

    override suspend fun saveSettings(settings: UserSettings) {
        saveLocalSettings(settings)
        try {
            settingsApi.saveSettings(settings.toRequest())
        } catch (e: Exception) {
        }
    }

//    override val settingsFlow: Flow<UserSettings> = dataStore.data.map { it.toSettings() ?: UserSettings() }

    override val settingsFlow: Flow<UserSettings> = dataStore.data
        .map { prefs ->
            val settings = prefs.toSettings() ?: UserSettings(theme = ThemeMode.LIGHT)
            settings
        }
        .catch { e ->
            emit(UserSettings(theme = ThemeMode.LIGHT))
        }

    private suspend fun saveLocalSettings(settings: UserSettings) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = settings.theme.name
        }
    }

    private fun Preferences.toSettings(): UserSettings? {
        val theme = this[THEME_KEY]?.let { ThemeMode.valueOf(it) } ?: return null
        return UserSettings(theme)
    }

    private fun UserSettings.toRequest() = SettingsRequest(theme.name)

}