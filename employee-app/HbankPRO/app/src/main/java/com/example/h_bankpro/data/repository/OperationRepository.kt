package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.FcmTokenRequest
import com.example.h_bankpro.data.dataSource.remote.OperationRemoteDataSource
import com.example.h_bankpro.data.dto.OperationDto
import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IOperationRepository
import kotlinx.coroutines.flow.Flow

class OperationRepository(
    private val remoteDataSource: OperationRemoteDataSource
) : IOperationRepository {
    override suspend fun getOperationsByAccount(
        accountId: String,
        pageable: Pageable,
        timeStart: String?,
        timeEnd: String?,
        operationType: String?
    ): RequestResult<PageResponse<OperationShortDto>> =
        remoteDataSource.getOperationsByAccount(accountId, pageable, timeStart, timeEnd, operationType)

    override suspend fun getOperationInfo(
        accountId: String,
        operationId: String
    ): RequestResult<OperationDto> = remoteDataSource.getOperationInfo(accountId, operationId)

    override fun getOperationsFlow(accountId: String): Flow<OperationShortDto> =
        remoteDataSource.getOperationsFlow(accountId)

    override fun disconnectWebSocket() = remoteDataSource.disconnectWebSocket()

    override suspend fun getExpiredLoanPayments(
        loanId: String,
        userId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>> =
        remoteDataSource.getExpiredLoanPayments(loanId, userId, pageable)

    override suspend fun sendFcmToken(
        userId: String,
        isManager: Boolean,
        fcmToken: String
    ): RequestResult<Unit> {
        return remoteDataSource.sendFcmToken(FcmTokenRequest(userId, isManager, fcmToken))
    }
}