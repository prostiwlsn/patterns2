package com.example.h_bank.data.network

import com.example.h_bank.data.dto.SettingsRequest
import com.example.h_bank.data.dto.SettingsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface SettingsApi {
    @GET("/api/Settings/bank/settings")
    suspend fun getSettings(): SettingsResponse

    @PUT("/api/Settings/bank/settings")
    suspend fun saveSettings(@Body settings: SettingsRequest)
}