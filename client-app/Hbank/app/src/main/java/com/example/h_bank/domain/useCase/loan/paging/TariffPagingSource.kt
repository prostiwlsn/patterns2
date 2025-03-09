package com.example.h_bank.domain.useCase.loan.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.loan.LoansPageResponse
import com.example.h_bank.domain.entity.loan.TariffEntity
import com.example.h_bank.domain.useCase.loan.GetTariffListUseCase

class TariffPagingSource(
    private val getTariffListUseCase: GetTariffListUseCase,
) : PagingSource<Int, TariffEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TariffEntity> {
        val page = params.key ?: 1
        val pageable = Pageable(page = page, size = params.loadSize)

        return when (val result = getTariffListUseCase(pageable)) {
            is RequestResult.Success<LoansPageResponse<TariffEntity>> -> {
                val operations = result.data.items
                LoadResult.Page(
                    data = operations,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (operations.isEmpty() || page >= result.data.pagesCount - 1) {
                        null
                    } else {
                        page + 1
                    }
                )
            }

            is RequestResult.Error<LoansPageResponse<TariffEntity>> ->
                LoadResult.Error(Exception(result.message ?: "Unknown error"))

            is RequestResult.NoInternetConnection<LoansPageResponse<TariffEntity>> ->
                LoadResult.Error(Exception("No internet connection"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TariffEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
