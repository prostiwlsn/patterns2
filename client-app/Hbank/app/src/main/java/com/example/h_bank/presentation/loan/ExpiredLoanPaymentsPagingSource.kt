package com.example.h_bank.presentation.loan

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.useCase.payment.GetExpiredLoanPaymentsUseCase
import com.example.h_bank.presentation.paymentHistory.model.OperationShortModel

class ExpiredLoanPaymentsPagingSource(
    private val loanId: String,
    private val getExpiredLoanPaymentsUseCase: GetExpiredLoanPaymentsUseCase
) : PagingSource<Int, OperationShortModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OperationShortModel> {
        val page = params.key ?: 0
        val pageable = Pageable(page = page, size = params.loadSize)

        return when (val result = getExpiredLoanPaymentsUseCase(loanId, pageable)) {
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

    override fun getRefreshKey(state: PagingState<Int, OperationShortModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}