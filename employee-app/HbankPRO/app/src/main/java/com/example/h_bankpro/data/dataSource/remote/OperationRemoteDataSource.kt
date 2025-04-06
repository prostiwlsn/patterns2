package com.example.h_bankpro.data.dataSource.remote

import com.example.h_bankpro.data.dto.OperationDto
import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.network.OperationApi
import com.example.h_bankpro.data.network.OperationWebSocketApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OperationRemoteDataSource(
    private val api: OperationApi,
    private val webSocketApi: OperationWebSocketApi
) {
    suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable,
        timeStart: String?,
        timeEnd: String?,
        operationType: String?
    ): RequestResult<PageResponse<OperationShortDto>> = withContext(Dispatchers.IO) {
        runResultCatching {
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

    suspend fun getOperationInfo(
        accountId: String,
        operationId: String
    ): RequestResult<OperationDto> = withContext(Dispatchers.IO) {
        runResultCatching { api.getOperationInfo(accountId, operationId) }
    }

    suspend fun getExpiredLoanPayments(
        loanId: String,
        userId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>> = withContext(Dispatchers.IO) {
        runResultCatching {
            api.expiredLoanPayment(
                loanId = loanId,
                userId = userId,
                size = pageable.size,
                page = pageable.page,
                sort = emptyList()
            )
        }
    }

    fun getOperationsFlow(accountId: String): Flow<OperationShortDto> {
        webSocketApi.disconnect()
        return webSocketApi.connect(accountId)
    }

    fun disconnectWebSocket() {
        webSocketApi.disconnect()
    }
}