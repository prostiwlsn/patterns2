package com.example.h_bank.data.repository.payment

import com.example.h_bank.data.FcmTokenRequest
import com.example.h_bank.data.dto.OperationRequestBody
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.payment.OperationDetailsDto
import com.example.h_bank.data.dto.payment.OperationDto
import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.dataSource.remote.OperationRemoteDataSource
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.filter.OperationFilterEntity
import com.example.h_bank.domain.repository.IOperationRepository
import kotlinx.coroutines.flow.Flow

class OperationRepository(
    private val remoteDataSource: OperationRemoteDataSource
) : IOperationRepository {

    override suspend fun getOperationsByAccount(
        filters: OperationFilterEntity,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>> {
        return remoteDataSource.getOperationsByAccount(filters, pageable)
    }

    override suspend fun getExpiredLoanPayments(
        loanId: String,
        pageable: Pageable
    ): RequestResult<PageResponse<OperationShortDto>> {
        return remoteDataSource.getExpiredLoanPayments(loanId, pageable)
    }

    override fun getOperationsFlow(filters: OperationFilterEntity): Flow<OperationShortDto> {
        return remoteDataSource.getOperationsFlow(filters)
    }

    override suspend fun getOperationDetails(
        accountId: String,
        operationId: String
    ): RequestResult<OperationDetailsDto> {
        return remoteDataSource.getOperationDetails(accountId, operationId)
    }

    override suspend fun getOperationInfo(operationId: String): RequestResult<OperationDto> {
        return remoteDataSource.getOperationInfo(operationId)
    }

    override suspend fun createOperation(request: OperationRequestBody): RequestResult<Unit> {
        return remoteDataSource.createOperation(request)
    }

    override fun disconnectWebSocket() {
        remoteDataSource.disconnectWebSocket()
    }

    override suspend fun sendFcmToken(
        userId: String,
        isManager: Boolean,
        fcmToken: String
    ): RequestResult<Unit> {
        return remoteDataSource.sendFcmToken(FcmTokenRequest(userId, isManager, fcmToken))
    }
}