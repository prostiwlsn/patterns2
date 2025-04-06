package com.example.h_bankpro.data.dataSource.remote

import com.example.h_bankpro.data.dto.CreditRatingDto
import com.example.h_bankpro.data.dto.LoanListResponseDto
import com.example.h_bankpro.data.dto.TariffListResponseDto
import com.example.h_bankpro.data.dto.TariffRequestDto
import com.example.h_bankpro.data.network.LoanApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoanRemoteDataSource(
    private val loanApi: LoanApi
) {
    suspend fun createTariff(request: TariffRequestDto): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            runResultCatching { loanApi.createTariff(request) }
        }

    suspend fun getTariffList(pageNumber: Int, pageSize: Int): RequestResult<TariffListResponseDto> =
        withContext(Dispatchers.IO) {
            runResultCatching { loanApi.getTariffList(pageNumber, pageSize) }
        }

    suspend fun deleteTariff(tariffId: String): RequestResult<Unit> = withContext(Dispatchers.IO) {
        runResultCatching { loanApi.deleteTariff(tariffId) }
    }

    suspend fun updateTariff(tariffId: String, request: TariffRequestDto): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            runResultCatching { loanApi.updateTariff(tariffId, request) }
        }

    suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto> = withContext(Dispatchers.IO) {
        runResultCatching { loanApi.getUserLoans(userId, pageNumber, pageSize) }
    }

    suspend fun getCreditRating(userId: String): RequestResult<CreditRatingDto> =
        withContext(Dispatchers.IO) {
            runResultCatching { loanApi.getCreditRating(userId) }
        }
}