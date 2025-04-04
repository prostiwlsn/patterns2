package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.CreditRatingDto
import com.example.h_bankpro.data.dto.LoanListResponseDto
import com.example.h_bankpro.data.dto.TariffListResponseDto
import com.example.h_bankpro.data.dto.TariffRequestDto
import com.example.h_bankpro.data.network.LoanApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.ILoanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoanRepository(
    private val loanApi: LoanApi
) : ILoanRepository {
    override suspend fun createTariff(request: TariffRequestDto): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching { loanApi.createTariff(request) }
        }

    override suspend fun getTariffList(
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<TariffListResponseDto> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching { loanApi.getTariffList(pageNumber, pageSize) }
    }

    override suspend fun deleteTariff(tariffId: String): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching { loanApi.deleteTariff(tariffId) }
        }

    override suspend fun updateTariff(
        tariffId: String,
        request: TariffRequestDto
    ): RequestResult<Unit> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching { loanApi.updateTariff(tariffId, request) }
    }

    override suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching { loanApi.getUserLoans(userId, pageNumber, pageSize) }
    }

    override suspend fun getCreditRating(
        userId: String
    ): RequestResult<CreditRatingDto> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching { loanApi.getCreditRating(userId) }
    }
}