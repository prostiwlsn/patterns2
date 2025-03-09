package com.example.h_bankpro.data.network

import retrofit2.http.POST

interface LogoutApi {
    @POST("logout")
    suspend fun logout()
}