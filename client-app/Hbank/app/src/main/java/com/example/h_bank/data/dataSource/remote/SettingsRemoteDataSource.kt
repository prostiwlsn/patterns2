package com.example.h_bank.data.dataSource.remote

import com.example.h_bank.data.dto.SettingsRequest
import com.example.h_bank.data.dto.SettingsResponse
import com.example.h_bank.data.network.SettingsApi

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