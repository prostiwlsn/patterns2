package com.example.h_bank.data.dataSource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.h_bank.data.UserSettings
import com.example.h_bank.data.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val HIDDEN_ACCOUNTS_KEY = stringSetPreferencesKey("hidden_accounts")
    }

    suspend fun getSettings(): UserSettings? {
        return dataStore.data.map { it.toSettings() }.first()
    }

    suspend fun saveSettings(settings: UserSettings) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = settings.theme.name
            prefs[HIDDEN_ACCOUNTS_KEY] = settings.hiddenAccounts
        }
    }

    val settingsFlow: Flow<UserSettings> = dataStore.data
        .map { prefs ->
            prefs.toSettings() ?: UserSettings(theme = ThemeMode.LIGHT)
        }
        .catch { emit(UserSettings(theme = ThemeMode.LIGHT)) }

    private fun Preferences.toSettings(): UserSettings? {
        val theme = this[THEME_KEY]?.let { ThemeMode.valueOf(it) } ?: return null
        val hiddenAccounts = this[HIDDEN_ACCOUNTS_KEY] ?: emptySet()
        return UserSettings(theme, hiddenAccounts)
    }
}