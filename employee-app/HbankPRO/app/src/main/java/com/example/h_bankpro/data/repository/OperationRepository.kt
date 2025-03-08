package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.OperationDto
import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.network.OperationApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IOperationRepository

class OperationRepository(
    private val api: OperationApi,
) : IOperationRepository {
    override suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable,
        timeStart: String?,
        timeEnd: String?,
        operationType: String?
    ): RequestResult<PageResponse<OperationShortDto>> {
        return runResultCatching {
            api.getOperationsByAccount(
                accountId,
                pageable.page,
                pageable.size,
                pageable.sort,
                timeStart,
                timeEnd,
                operationType
            )
        }
    }

    override suspend fun getOperationInfo(
        accountId: String,
        operationId: String
    ): RequestResult<OperationDto> {
        return runResultCatching {
            api.getOperationInfo(accountId, operationId)
        }
    }
}