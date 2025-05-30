package com.example.h_bank.data.dataSource.remote

import com.example.h_bank.data.FcmTokenRequest
import com.example.h_bank.data.dto.OperationRequestBody
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.payment.OperationDetailsDto
import com.example.h_bank.data.dto.payment.OperationDto
import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.network.OperationApi
import com.example.h_bank.data.network.OperationWebSocketApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.filter.OperationFilterEntity
import com.example.h_bank.domain.entity.filter.OperationType.Companion.getQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import java.time.ZoneOffset

class OperationRemoteDataSource(
    private val api: OperationApi,
    private val webSocketApi: OperationWebSocketApi
) {
    suspend fun getOperationsByAccount(
        filters: OperationFilterEntity,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>> = withContext(Dispatchers.IO) {
        val accountId =
            filters.accountId ?: return@withContext RequestResult.Error(404, "Account not found")
        return@withContext retry(3, delayMillis = 1000) {
            runResultCatching(noConnectionCatching = true) {
                api.getOperationsByAccount(
                    accountId = accountId,
                    timeStart = filters.startDate?.toInstant(ZoneOffset.UTC),
                    timeEnd = filters.endDate?.toInstant(ZoneOffset.UTC),
                    operationType = filters.operationType?.getQuery(),
                    size = pageable.size,
                    page = pageable.page,
                    sort = emptyList()
                )
            }
        }
    }

    suspend fun getExpiredLoanPayments(
        loanId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            api.expiredLoanPayment(
                loanId = loanId,
                size = pageable.size,
                page = pageable.page,
                sort = emptyList()
            )
        }
    }

    fun getOperationsFlow(filters: OperationFilterEntity): Flow<OperationShortDto> {
        val accountId = filters.accountId ?: return emptyFlow()
        webSocketApi.disconnect()
        return webSocketApi.connect(accountId)
    }

    suspend fun getOperationDetails(
        accountId: String,
        operationId: String
    ): RequestResult<OperationDetailsDto> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            api.getOperationDetails(accountId = accountId, operationId = operationId)
        }
    }

    suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching {
                api.getOperationInfo(operationId)
            }
        }

    suspend fun createOperation(request: OperationRequestBody): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching {
                api.createOperation(request)
            }
        }

    suspend fun sendFcmToken(request: FcmTokenRequest) = withContext(Dispatchers.IO) {
        runResultCatching { api.sendFcmToken(request) }
    }

    fun disconnectWebSocket() {
        webSocketApi.disconnect()
    }

    private suspend fun <T> retry(
        times: Int,
        delayMillis: Long,
        block: suspend () -> RequestResult<T>
    ): RequestResult<T> {
        repeat(times - 1) { attempt ->
            val result = block()
            if (result is RequestResult.Success) return result
            delay(delayMillis)
        }
        return block()
    }
}