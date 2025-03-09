package com.example.h_bank.presentation.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.h_bank.data.Loan
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.useCase.loan.GetUserLoansUseCase

class LoansPagingSource(
    private val getUserLoansUseCase: GetUserLoansUseCase,
    private val userId: String
) : PagingSource<Int, Loan>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Loan> {
        val page = params.key ?: 1
        return when (val result =
            getUserLoansUseCase(userId, pageNumber = page, pageSize = params.loadSize)) {
            is RequestResult.Success -> {
                val data = result.data.items
                val totalPages = result.data.pagesCount
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isNotEmpty() && page < totalPages) page + 1 else null
                )
            }

            is RequestResult.Error -> {
                LoadResult.Error(Exception(result.message))
            }

            is RequestResult.NoInternetConnection -> {
                LoadResult.Error(Exception("No internet"))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Loan>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}