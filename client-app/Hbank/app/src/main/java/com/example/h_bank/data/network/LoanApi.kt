package com.example.h_bank.data.network

import com.example.h_bank.data.dto.CreditRatingDto
import com.example.h_bank.data.dto.loan.GetLoanDto
import com.example.h_bank.data.dto.loan.LoanListResponseDto
import com.example.h_bank.data.dto.loan.TariffListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LoanApi {
    @GET("loan/tariff")
    suspend fun getTariffList(
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): TariffListResponseDto

    @GET("loan/{userId}/list")
    suspend fun getUserLoans(
        @Path("userId") userId: String,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): LoanListResponseDto

    @POST("loan")
    suspend fun getLoan(
        @Body request: GetLoanDto,
    )

    @GET("loan/{userId}/creditRating")
    suspend fun getCreditRating(
        @Path("userId") userId: String,
    ): CreditRatingDto
}