package com.example.h_bankpro.data.dataSource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.h_bankpro.data.ThemeMode
import com.example.h_bankpro.data.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }

    suspend fun getSettings(): UserSettings? {
        return dataStore.data.map { it.toSettings() }.first()
    }

    suspend fun saveSettings(settings: UserSettings) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = settings.theme.name
        }
    }

    val settingsFlow: Flow<UserSettings> = dataStore.data
        .map { prefs ->
            prefs.toSettings() ?: UserSettings(theme = ThemeMode.LIGHT)
        }
        .catch { emit(UserSettings(theme = ThemeMode.LIGHT)) }

    private fun Preferences.toSettings(): UserSettings? {
        val theme = this[THEME_KEY]?.let { ThemeMode.valueOf(it) } ?: return null
        return UserSettings(theme)
    }
}