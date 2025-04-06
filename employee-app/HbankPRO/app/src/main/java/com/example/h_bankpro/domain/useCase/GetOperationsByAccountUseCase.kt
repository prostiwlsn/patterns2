package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.OperationTypeFilter
import com.example.h_bankpro.data.dto.OperationShortDto
import com.example.h_bankpro.data.dto.PageResponse
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.data.utils.mapSuccess
import com.example.h_bankpro.domain.model.OperationShort
import com.example.h_bankpro.domain.repository.IOperationRepository
import com.example.h_bankpro.presentation.account.OperationsFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId

class GetOperationsByAccountUseCase(
    private val operationRepository: IOperationRepository
) {
    suspend operator fun invoke(
        accountId: String,
        pageable: Pageable = Pageable(),
        timeStart: String? = null,
        timeEnd: String? = null,
        operationType: String? = null
    ): RequestResult<PageResponse<OperationShort>> {
        return operationRepository.getOperationsByAccount(
            accountId,
            pageable,
            timeStart,
            timeEnd,
            operationType
        ).mapSuccess { pageResponse ->
            val filteredContent = pageResponse.content
                .filter { passesFilters(it, timeStart, timeEnd, operationType) }
                .map { it.toDomain() }
            PageResponse(
                content = filteredContent,
                totalPages = pageResponse.totalPages,
                totalElements = filteredContent.size.toLong(),
                first = pageResponse.first,
                last = filteredContent.size < pageable.size,
                size = pageResponse.size,
                number = pageResponse.number,
                sort = pageResponse.sort,
                pageable = pageResponse.pageable,
                numberOfElements = filteredContent.size,
                empty = filteredContent.isEmpty()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getOperationsFlow(accountId: String, filters: OperationsFilters): Flow<OperationShort> {
        return operationRepository.getOperationsFlow(accountId)
            .filter { passesFilters(it, filters) }
            .map {
                it.toDomain()
            }
    }

    private fun passesFilters(dto: OperationShortDto, filters: OperationsFilters): Boolean {
        val operationDateTime = dto.transactionDateTime
            .toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val typeMatches = when (filters.operationType) {
            OperationTypeFilter.All -> true
            is OperationTypeFilter.Specific -> filters.operationType.type.name.lowercase() == dto.operationType.name.lowercase()
        }

        val dateMatches = filters.dateRange.let { (start, end) ->
            val startTime = start?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
            val endTime = end?.atTime(23, 59, 59)?.atZone(ZoneId.systemDefault())?.toInstant()
            (startTime == null || dto.transactionDateTime.toJavaInstant() >= startTime) &&
                    (endTime == null || dto.transactionDateTime.toJavaInstant() <= endTime)
        }

        return typeMatches && dateMatches
    }

    private fun passesFilters(dto: OperationShortDto, timeStart: String?, timeEnd: String?, operationType: String?): Boolean {
        val operationDateTime = dto.transactionDateTime
            .toJavaInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val typeMatches = operationType?.let { it.lowercase() == dto.operationType.name.lowercase() } ?: true
        val startTime = timeStart?.let { Instant.parse(it) }
        val endTime = timeEnd?.let { Instant.parse(it) }
        val dateMatches = (startTime == null || dto.transactionDateTime >= startTime) &&
                (endTime == null || dto.transactionDateTime <= endTime)

        return typeMatches && dateMatches
    }

    fun disconnect() {
        operationRepository.disconnectWebSocket()
    }
}
