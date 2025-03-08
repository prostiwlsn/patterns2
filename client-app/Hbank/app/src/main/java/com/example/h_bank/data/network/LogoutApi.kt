package com.example.h_bank.data.network

import retrofit2.http.POST

interface LogoutApi {
    @POST("logout")
    suspend fun logout()
}