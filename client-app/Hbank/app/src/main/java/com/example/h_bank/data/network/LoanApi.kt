package com.example.h_bank.data.network

import com.example.h_bank.data.dto.loan.LoanListResponseDto
import com.example.h_bank.data.dto.loan.TariffListResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LoanApi {
    @GET("loan/tariff")
    suspend fun getTariffList(
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): TariffListResponseDto

    /*@POST("/api/loan/tariff")
    suspend fun createTariff(
        @Body request: TariffRequestDto
    )*/

    @DELETE("loan/tariff")
    suspend fun deleteTariff(
        @Query("tariffId") tariffId: String
    )

    /*@PUT("/api/loan/tariff")
    suspend fun updateTariff(
        @Query("tariffId") tariffId: String,
        @Body request: TariffRequestDto
    )*/

    @GET("loan/{userId}/list")
    suspend fun getUserLoans(
        @Path("userId") userId: String,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): LoanListResponseDto
}