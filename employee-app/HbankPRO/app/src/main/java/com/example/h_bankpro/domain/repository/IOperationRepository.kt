package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.dto.OperationDto
import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.utils.RequestResult
import kotlinx.coroutines.flow.Flow

interface IOperationRepository {
    suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable,
        timeStart: String? = null,
        timeEnd: String? = null,
        operationType: String? = null
    ): RequestResult<PageResponse<OperationShortDto>>

    fun getOperationsFlow(accountId: String): Flow<OperationShortDto>

    suspend fun getOperationInfo(
        accountId: String,
        operationId: String
    ): RequestResult<OperationDto>

    fun disconnectWebSocket()

    suspend fun getExpiredLoanPayments(
        loanId: String,
        userId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>>
}