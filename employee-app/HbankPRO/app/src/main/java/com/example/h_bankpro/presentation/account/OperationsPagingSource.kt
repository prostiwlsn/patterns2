package com.example.h_bankpro.presentation.account

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.h_bankpro.data.OperationType
import com.example.h_bankpro.data.OperationTypeFilter
import com.example.h_bankpro.data.dto.Pageable
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.model.OperationShort
import com.example.h_bankpro.domain.useCase.GetOperationsByAccountUseCase
import java.time.LocalDate as JavaLocalDate
import java.time.ZoneId

class OperationsPagingSource(
    private val accountId: String,
    private val filters: OperationsFilters,
    private val getOperationsByAccountUseCase: GetOperationsByAccountUseCase
) : PagingSource<Int, OperationShort>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OperationShort> {
        val page = params.key ?: 0
        val pageable = Pageable(page = page, size = params.loadSize)

        val timeStart = filters.dateRange.first?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toString()
        val timeEnd = filters.dateRange.second?.atTime(23, 59, 59)?.atZone(ZoneId.systemDefault())?.toInstant()?.toString()
        val operationType = when (val type = filters.operationType) {
            is OperationTypeFilter.All -> null
            is OperationTypeFilter.Specific -> when (type.type) {
                OperationType.REPLENISHMENT -> "replenishment"
                OperationType.WITHDRAWAL -> "withdrawal"
                OperationType.TRANSFER -> "transfer"
                OperationType.LOAN_REPAYMENT -> "loan_payment"
            }
        }

        return when (val result = getOperationsByAccountUseCase(accountId, pageable, timeStart, timeEnd, operationType)) {
            is RequestResult.Success -> {
                val operations = result.data.content
                LoadResult.Page(
                    data = operations,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (operations.isEmpty() || page >= result.data.totalPages - 1) null else page + 1
                )
            }
            is RequestResult.Error -> LoadResult.Error(Exception(result.message ?: "Unknown error"))
            is RequestResult.NoInternetConnection -> LoadResult.Error(Exception("No internet connection"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OperationShort>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

data class OperationsFilters(
    val operationType: OperationTypeFilter = OperationTypeFilter.All,
    val dateRange: Pair<JavaLocalDate?, JavaLocalDate?> = null to null
)