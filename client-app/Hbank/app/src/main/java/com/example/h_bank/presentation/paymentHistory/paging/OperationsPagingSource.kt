package com.example.h_bank.presentation.paymentHistory.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.useCase.payment.GetOperationsHistoryUseCase
import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel

class OperationsPagingSource(
    private val getOperationsHistoryUseCase: GetOperationsHistoryUseCase,
) : PagingSource<Int, OperationShortModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OperationShortModel> {
        val page = params.key ?: 0
        val pageable = Pageable(page = page, size = params.loadSize)

        return when (val result = getOperationsHistoryUseCase(pageable)) {
            is RequestResult.Success<PageResponse<OperationShortModel>> -> {
                val operations = result.data.content
                LoadResult.Page(
                    data = operations,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (operations.isEmpty() || page >= result.data.totalPages - 1) null else page + 1
                )
            }

            is RequestResult.Error<PageResponse<OperationShortModel>> ->
                LoadResult.Error(Exception(result.message ?: "Unknown error"))

            is RequestResult.NoInternetConnection<PageResponse<OperationShortModel>> ->
                LoadResult.Error(Exception("No internet connection"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OperationShortModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}