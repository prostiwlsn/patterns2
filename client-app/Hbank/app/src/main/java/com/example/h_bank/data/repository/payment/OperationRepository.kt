package com.example.h_bank.data.repository.payment

import com.example.h_bank.data.dto.OperationRequestBody
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.payment.OperationDetailsDto
import com.example.h_bank.data.dto.payment.OperationDto
import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.network.OperationApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.filter.OperationFilterEntity
import com.example.h_bank.domain.entity.filter.OperationType.Companion.getQuery
import com.example.h_bank.domain.repository.IOperationRepository
import java.time.ZoneOffset

class OperationRepository(
    private val api: OperationApi,
) : IOperationRepository {
    override suspend fun getOperationsByAccount(
        filters: OperationFilterEntity,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>> {
        return runResultCatching {
            api.getOperationsByAccount(
                accountId = filters.accountId.orEmpty(),
                timeStart = filters.startDate?.toInstant(ZoneOffset.UTC),
                timeEnd = filters.endDate?.toInstant(ZoneOffset.UTC),
                operationType = filters.operationType?.getQuery(),
                size = pageable.size,
                page = pageable.page,
                sort = emptyList()
            )
        }
    }

    override suspend fun getOperationDetails(
        accountId: String,
        operationId: String,
    ): RequestResult<OperationDetailsDto> {
        return runResultCatching {
            api.getOperationDetails(
                accountId = accountId,
                operationId = operationId,
            )
        }
    }

    override suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto> {
        return runResultCatching {
            api.getOperationInfo(operationId)
        }
    }

    override suspend fun createOperation(request: OperationRequestBody): RequestResult<Unit> {
        return runResultCatching {
            api.createOperation(request)
        }
    }
}