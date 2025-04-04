package com.example.h_bank.data.repository.loan

import com.example.h_bank.data.dto.CreditRatingDto
import com.example.h_bank.data.dto.loan.GetLoanDto
import com.example.h_bank.data.dto.loan.LoanListResponseDto
import com.example.h_bank.data.dto.loan.TariffListResponseDto
import com.example.h_bank.data.network.LoanApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.loan.ILoanRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoanRemoteRepository(
    private val loanApi: LoanApi
) : ILoanRemoteRepository {
    override suspend fun getTariffList(
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<TariffListResponseDto> =  withContext(Dispatchers.IO) {
        return@withContext runResultCatching { loanApi.getTariffList(pageNumber, pageSize) }
    }

    override suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto> =  withContext(Dispatchers.IO) {
        return@withContext runResultCatching { loanApi.getUserLoans(userId, pageNumber, pageSize) }
    }

    override suspend fun getLoan(
        request: GetLoanDto,
    ): RequestResult<Unit> =  withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            loanApi.getLoan(request)
        }
    }

    override suspend fun getCreditRating(
        userId: String
    ): RequestResult<CreditRatingDto> =  withContext(Dispatchers.IO) {
        return@withContext runResultCatching { loanApi.getCreditRating(userId) }
    }
}