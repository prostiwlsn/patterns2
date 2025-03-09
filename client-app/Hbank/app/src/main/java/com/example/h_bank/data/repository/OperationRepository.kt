package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.OperationRquestBody
import com.example.h_bank.data.dto.OperationDto
import com.example.h_bank.data.dto.OperationShortDto
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.network.OperationApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IOperationRepository

class OperationRepository(
    private val api: OperationApi,
) : IOperationRepository {
    override suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>> {
        return runResultCatching {
            api.getOperationsByAccount(accountId, pageable.page, pageable.size, pageable.sort)
        }
    }

    override suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto> {
        return runResultCatching {
            api.getOperationInfo(operationId)
        }
    }

    override suspend fun createOperation(request: OperationRquestBody): RequestResult<Unit> {
        return runResultCatching {
            api.createOperation(request)
        }
    }
}