package com.example.h_bank.data.network

import com.example.h_bank.data.dto.AccountDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountApi {
    @GET("account/{userId}/list")
    suspend fun getUserAccounts(
        @Path("userId") userId: String,
    ): List<AccountDto>
}