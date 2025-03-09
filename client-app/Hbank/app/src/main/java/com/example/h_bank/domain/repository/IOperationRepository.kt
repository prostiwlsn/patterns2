package com.example.h_bank.domain.repository

import com.example.h_bank.data.dto.OperationRquestBody
import com.example.h_bank.data.dto.OperationDto
import com.example.h_bank.data.dto.OperationShortDto
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.utils.RequestResult

interface IOperationRepository {
    suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>>

    suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto>

    suspend fun createOperation(request: OperationRquestBody): RequestResult<Unit>
}