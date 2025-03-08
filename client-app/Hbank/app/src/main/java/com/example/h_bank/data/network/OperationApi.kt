package com.example.h_bank.data.network

import com.example.h_bank.data.dto.OperationCreationRequestDto
import com.example.h_bank.data.dto.OperationDto
import com.example.h_bank.data.dto.OperationShortDto
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.utils.RequestResult
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
        @Query("sort") sort: List<String>
    ): PageResponse<OperationShortDto>

    @GET("operation/{operationId}")
    suspend fun getOperationInfo(
        @Path("operationId") operationId: String,
    ): OperationDto

    @POST("operation")
    suspend fun createOperation(
        @Body request: OperationCreationRequestDto
    )
}