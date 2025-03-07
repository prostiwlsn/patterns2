package com.example.h_bank.domain.repository

import com.example.h_bank.data.utils.RequestResult

interface IOperationRepository {
    suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>>

    suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto>
}