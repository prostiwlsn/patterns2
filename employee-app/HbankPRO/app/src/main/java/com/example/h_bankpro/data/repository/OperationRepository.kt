package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.OperationDto
import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.network.OperationApi
import com.example.h_bankpro.data.network.OperationWebSocketApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IOperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OperationRepository(
    private val api: OperationApi,
    private val webSocketApi: OperationWebSocketApi
) : IOperationRepository {
    override suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable,
        timeStart: String?,
        timeEnd: String?,
        operationType: String?
    ): RequestResult<PageResponse<OperationShortDto>> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
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
    ): RequestResult<OperationDto> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            api.getOperationInfo(accountId, operationId)
        }
    }

    override fun getOperationsFlow(accountId: String): Flow<OperationShortDto> {
        webSocketApi.disconnect()
        return webSocketApi.connect(accountId)
    }

    override fun disconnectWebSocket() {
        webSocketApi.disconnect()
    }
}