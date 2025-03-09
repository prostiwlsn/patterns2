package com.example.h_bank.data.network

import com.example.h_bank.data.dto.payment.OperationRquestBody
import com.example.h_bank.data.dto.payment.OperationDto
import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.payment.OperationDetailsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

interface OperationApi {
    @GET("operation/byAccount/{accountId}")
    suspend fun getOperationsByAccount(
        @Path("accountId") accountId: String,
        @Query("timeStart") timeStart: Instant?,
        @Query("timeEnd") timeEnd: Instant?,
        @Query("operationType") operationType: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: List<String>
    ): PageResponse<OperationShortDto>

    @GET("operation/{accountId}/{operationId}")
    suspend fun getOperationDetails(
        @Path("accountId") accountId: String,
        @Path("operationId") operationId: String,
    ): OperationDetailsDto

    @GET("operation/{operationId}")
    suspend fun getOperationInfo(
        @Path("operationId") operationId: String,
    ): OperationDto

    @POST("operation")
    suspend fun createOperation(
        @Body request: OperationRquestBody
    )
}