package com.example.h_bank.domain.repository

import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.payment.OperationDetailsDto
import com.example.h_bank.data.dto.payment.OperationDto
import com.example.h_bank.data.dto.payment.OperationRquestBody
import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.filter.OperationFilterEntity

interface IOperationRepository {
    suspend fun getOperationsByAccount(
        filters: OperationFilterEntity,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>>

    suspend fun getOperationDetails(
        accountId: String,
        operationId: String,
    ): RequestResult<OperationDetailsDto>

    suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto>

    suspend fun createOperation(request: OperationRquestBody): RequestResult<Unit>
}