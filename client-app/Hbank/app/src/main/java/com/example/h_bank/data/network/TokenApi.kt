package com.example.h_bank.data.network

import com.example.h_bank.data.dto.RefreshRequestDto
import com.example.h_bank.data.dto.TokenDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenApi {
    @POST("refresh")
    suspend fun refreshToken(
        @Body request: RefreshRequestDto,
        @Header("Authorization") accessToken: String
    ): TokenDto
}