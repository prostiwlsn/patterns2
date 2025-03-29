package com.example.h_bank.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.h_bank.data.UserSettings
import com.example.h_bank.data.ThemeMode
import com.example.h_bank.data.dto.SettingsRequest
import com.example.h_bank.data.dto.SettingsResponse
import com.example.h_bank.data.dto.toDomain
import com.example.h_bank.data.network.SettingsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

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
        private val HIDDEN_ACCOUNTS_KEY = stringSetPreferencesKey("hidden_accounts")
    }

    override suspend fun getSettings(): UserSettings {
        println("SettingsRepository: Attempting to fetch settings from server")
        try {
            val serverSettings = settingsApi.getSettings().toDomain()
            println("SettingsRepository: Loaded from server = $serverSettings")
            saveLocalSettings(serverSettings)
            return serverSettings
        } catch (e: Exception) {
            println("SettingsRepository: Server fetch failed: $e")
            val localSettings = dataStore.data.first().toSettings()
            println("SettingsRepository: Loaded from local = $localSettings")
            return if (localSettings != null) {
                localSettings
            } else {
                val defaultSettings = UserSettings(theme = ThemeMode.LIGHT)
                println("SettingsRepository: Using default = $defaultSettings")
                saveLocalSettings(defaultSettings)
                defaultSettings
            }
        }
    }

    override suspend fun saveSettings(settings: UserSettings) {
        saveLocalSettings(settings)
        println("SettingsRepository: Saved settings locally = $settings")
        try {
            settingsApi.saveSettings(settings.toRequest())
            println("SettingsRepository: Saved settings to server = $settings")
        } catch (e: Exception) {
            println("SettingsRepository: Failed to save settings to server: $e")
        }
    }

//    override val settingsFlow: Flow<UserSettings> = dataStore.data.map { it.toSettings() ?: UserSettings() }

    override val settingsFlow: Flow<UserSettings> = dataStore.data
        .map { prefs ->
            val settings = prefs.toSettings() ?: UserSettings(theme = ThemeMode.LIGHT)
            println("SettingsFlow: Emitting = $settings")
            settings
        }
        .catch { e ->
            println("SettingsFlow error: $e")
            emit(UserSettings(theme = ThemeMode.LIGHT))
        }

    private suspend fun saveLocalSettings(settings: UserSettings) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = settings.theme.name
            prefs[HIDDEN_ACCOUNTS_KEY] = settings.hiddenAccounts
            println("SettingsRepository: Edited DataStore with theme = ${settings.theme}")
        }
    }

    private fun Preferences.toSettings(): UserSettings? {
        val theme = this[THEME_KEY]?.let { ThemeMode.valueOf(it) } ?: return null
        val hiddenAccounts = this[HIDDEN_ACCOUNTS_KEY] ?: emptySet()
        return UserSettings(theme, hiddenAccounts)
    }

    private fun UserSettings.toRequest() = SettingsRequest(theme.name, hiddenAccounts.toList())

}