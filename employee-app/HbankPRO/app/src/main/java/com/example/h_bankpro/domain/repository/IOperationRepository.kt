package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.dto.OperationDto
import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.utils.RequestResult

interface IOperationRepository {
    suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>>

    suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto>
}