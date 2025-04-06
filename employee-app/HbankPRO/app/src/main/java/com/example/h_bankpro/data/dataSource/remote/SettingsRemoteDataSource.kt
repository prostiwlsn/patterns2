package com.example.h_bankpro.data.dataSource.remote

import com.example.h_bankpro.data.dto.SettingsRequest
import com.example.h_bankpro.data.dto.SettingsResponse
import com.example.h_bankpro.data.network.SettingsApi

class SettingsRemoteDataSource(
    private val settingsApi: SettingsApi
) {
    suspend fun getSettings(): SettingsResponse {
        return settingsApi.getSettings()
    }

    suspend fun saveSettings(settings: SettingsRequest) {
        settingsApi.saveSettings(settings)
    }
}