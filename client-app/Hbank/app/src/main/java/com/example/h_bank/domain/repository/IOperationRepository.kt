package com.example.h_bank.domain.repository

import com.example.h_bank.data.dto.OperationRequestBody
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.payment.OperationDetailsDto
import com.example.h_bank.data.dto.payment.OperationDto
import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.filter.OperationFilterEntity
import kotlinx.coroutines.flow.Flow

interface IOperationRepository {
    suspend fun getOperationsByAccount(
        filters: OperationFilterEntity,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>>

    suspend fun getExpiredLoanPayments(
        loanId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>>

    fun getOperationsFlow(filters: OperationFilterEntity): Flow<OperationShortDto>

    suspend fun getOperationDetails(
        accountId: String,
        operationId: String,
    ): RequestResult<OperationDetailsDto>

    suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto>

    suspend fun createOperation(request: OperationRequestBody): RequestResult<Unit>

    suspend fun sendFcmToken(
        userId: String,
        isManager: Boolean,
        fcmToken: String
    ): RequestResult<Unit>

    fun disconnectWebSocket()
}