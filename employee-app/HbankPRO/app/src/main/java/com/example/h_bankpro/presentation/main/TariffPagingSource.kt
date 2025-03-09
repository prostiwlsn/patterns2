package com.example.h_bankpro.presentation.main

import android.util.Log
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
                val data = result.data.items
                val totalPages = result.data.pagesCount
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isNotEmpty() && page < totalPages) page + 1 else null
                )
            }

            is RequestResult.Error -> {
                Log.e("TariffsPagingSource", "Error: ${result.message}")
                LoadResult.Error(Exception(result.message))
            }

            is RequestResult.NoInternetConnection -> {
                Log.e("TariffsPagingSource", "No internet")
                LoadResult.Error(Exception("No internet"))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Tariff>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}