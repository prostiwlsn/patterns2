package com.example.h_bankpro.data.network

import com.example.h_bankpro.data.FcmTokenRequest
import com.example.h_bankpro.data.dto.OperationDto
import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OperationApi {
    @GET("operation/byAccount/{accountId}")
    suspend fun getOperationsByAccount(
        @Path("accountId") accountId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>,
        @Query("timeStart") timeStart: String? = null,
        @Query("timeEnd") timeEnd: String? = null,
        @Query("operationType") operationType: String? = null
    ): PageResponse<OperationShortDto>

    @GET("operation/{accountId}/{operationId}")
    suspend fun getOperationInfo(
        @Path("accountId") accountId: String,
        @Path("operationId") operationId: String,
    ): OperationDto

    @GET("operation/expiredLoanPayment")
    suspend fun expiredLoanPayment(
        @Query("loanAccountId") loanId: String,
        @Query("userId") userId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>
    ): PageResponse<OperationShortDto>

    @POST("/api/fcm")
    suspend fun sendFcmToken(@Body request: FcmTokenRequest)
}