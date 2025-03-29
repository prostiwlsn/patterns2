package com.example.h_bank.data.network

import com.example.h_bank.data.dto.AccountDto
import com.example.h_bank.data.dto.CurrencyDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountApi {
    @GET("account/{userId}/list")
    suspend fun getUserAccounts(
        @Path("userId") userId: String,
    ): List<AccountDto>

    @POST("account")
    suspend fun openAccount(
        @Query("accountId") currency: CurrencyDto,
    )

    @DELETE("account")
    suspend fun closeAccount(
        @Query("accountId") accountId: String,
    )

    @GET("account/{accountNumber}")
    suspend fun getAccountIdByNumber(
        @Path("accountNumber") accountNumber: String,
    ): String
}