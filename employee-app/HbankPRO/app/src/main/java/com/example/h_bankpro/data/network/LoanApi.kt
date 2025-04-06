package com.example.h_bankpro.data.network

import com.example.h_bankpro.data.dto.CreditRatingDto
import com.example.h_bankpro.data.dto.LoanListResponseDto
import com.example.h_bankpro.data.dto.TariffDto
import com.example.h_bankpro.data.dto.TariffListResponseDto
import com.example.h_bankpro.data.dto.TariffRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface LoanApi {
    @GET("loan/tariff")
    suspend fun getTariffList(
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): TariffListResponseDto

    @POST("/api/loan/tariff")
    suspend fun createTariff(
        @Body request: TariffRequestDto
    )

    @DELETE("/api/loan/tariff")
    suspend fun deleteTariff(
        @Query("tariffId") tariffId: String
    )

    @PUT("/api/loan/tariff")
    suspend fun updateTariff(
        @Query("tariffId") tariffId: String,
        @Body request: TariffRequestDto
    )

    @GET("/api/loan/{userId}/list")
    suspend fun getUserLoans(
        @Path("userId") userId: String,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): LoanListResponseDto

    @GET("loan/{userId}/creditRating")
    suspend fun getCreditRating(
        @Path("userId") userId: String,
    ): CreditRatingDto
}