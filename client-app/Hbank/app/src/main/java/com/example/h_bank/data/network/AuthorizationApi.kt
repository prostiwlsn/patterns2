package com.example.h_bank.data.network

import com.example.h_bank.data.dto.LoginDto
import com.example.h_bank.data.dto.RefreshRequestDto
import com.example.h_bank.data.dto.RegisterDto
import com.example.h_bank.data.dto.TokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationApi {
    @POST("login")
    suspend fun login(
        @Body request: LoginDto
    ): TokenDto

    @POST("register")
    suspend fun register(
        @Body request: RegisterDto
    ): TokenDto

    @POST("refresh")
    suspend fun refreshToken(@Body request: RefreshRequestDto): TokenDto
}