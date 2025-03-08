package com.example.h_bankpro.presentation.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.model.Tariff
import com.example.h_bankpro.domain.useCase.GetTariffListUseCase

class TariffPagingSource(
    private val getTariffListUseCase: GetTariffListUseCase
) : PagingSource<Int, Tariff>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tariff> {
        val page = params.key ?: 1
        return when (val result =
            getTariffListUseCase(pageNumber = page, pageSize = params.loadSize)) {
            is RequestResult.Success -> {
                val data = result.data
                val response = getTariffListUseCase(pageNumber = page, pageSize = params.loadSize)
                val totalPages = (response as? RequestResult.Success)?.data?.let {

                    getTariffListUseCase(pageNumber = page, pageSize = params.loadSize).let { r ->
                        if (r is RequestResult.Success) r.data.size else 0
                    }
                    val assumedTotalPages = 10
                    assumedTotalPages
                } ?: 1
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (data.isNotEmpty() && page < totalPages - 1) page + 1 else null
                )
            }

            is RequestResult.Error -> LoadResult.Error(Exception(result.message))
            is RequestResult.NoInternetConnection -> LoadResult.Error(Exception("No internet"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Tariff>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}