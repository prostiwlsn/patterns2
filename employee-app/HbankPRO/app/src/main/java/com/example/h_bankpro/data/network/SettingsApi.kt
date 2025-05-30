package com.example.h_bankpro.data.network

import com.example.h_bankpro.data.dto.SettingsRequest
import com.example.h_bankpro.data.dto.SettingsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface SettingsApi {
    @GET("/api/Settings/admin/settings")
    suspend fun getSettings(): SettingsResponse

    @PUT("/api/Settings/admin/settings")
    suspend fun saveSettings(@Body settings: SettingsRequest)
}