package com.example.h_bank.domain.useCase.payment

import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.payment.OperationShortDto
import com.example.h_bank.data.dto.payment.OperationTypeDto
import com.example.h_bank.data.dto.payment.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.entity.filter.OperationFilterEntity
import com.example.h_bank.domain.entity.filter.OperationType
import com.example.h_bank.domain.repository.IOperationRepository
import com.example.h_bank.domain.repository.payment.IPaymentStorageRepository
import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId

class GetOperationsHistoryUseCase(
    private val operationRepository: IOperationRepository,
    private val storageRepository: IPaymentStorageRepository,
) {
    suspend fun getInitialOperations(pageable: Pageable): RequestResult<PageResponse<OperationShortModel>> {
        val filters = storageRepository.getFilters()
        return operationRepository.getOperationsByAccount(filters, pageable)
            .mapSuccess { pageResponse ->
                val filteredContent = pageResponse.content
                    .filter { passesFilters(it, filters) }
                    .map { it.toDomain() }

                PageResponse(
                    content = filteredContent,
                    totalPages = pageResponse.totalPages,
                    totalElements = filteredContent.size.toLong(),
                    first = pageResponse.first,
                    last = filteredContent.size < pageable.size,
                    size = pageable.size,
                    number = pageResponse.number,
                    sort = pageResponse.sort,
                    pageable = pageResponse.pageable,
                    numberOfElements = filteredContent.size,
                    empty = filteredContent.isEmpty()
                )
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getOperationsFlow(): Flow<OperationShortModel> {
        return storageRepository.getFiltersFlow()
            .filter { it.accountId != null }
            .distinctUntilChanged()
            .flatMapLatest { filters ->
                operationRepository.getOperationsFlow(filters)
                    .filter { passesFilters(it, filters) }
                    .map {
                        it.toDomain()
                    }
            }
    }

    private fun passesFilters(dto: OperationShortDto, filters: OperationFilterEntity): Boolean {
        val operationDateTime = dto.transactionDateTime
            .toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val operationTypeMatches = filters.operationType?.let { filterType ->
            mapOperationTypeDtoToDomain(dto.operationType) == filterType
        } ?: true

        return operationTypeMatches &&
                filters.startDate?.let { operationDateTime >= it } != false &&
                filters.endDate?.let { operationDateTime <= it } != false
    }

    private fun mapOperationTypeDtoToDomain(dto: OperationTypeDto): OperationType {
        return when (dto) {
            OperationTypeDto.REPLENISHMENT -> OperationType.REPLENISHMENT
            OperationTypeDto.WITHDRAWAL -> OperationType.WITHDRAWAL
            OperationTypeDto.TRANSFER -> OperationType.TRANSFER
            OperationTypeDto.LOAN_REPAYMENT -> OperationType.LOAN_REPAYMENT
        }
    }

    fun disconnect() {
        operationRepository.disconnectWebSocket()
    }
}