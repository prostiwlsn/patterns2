package com.example.h_bank.data.dataSource.remote

import com.example.h_bank.data.dto.CreditRatingDto
import com.example.h_bank.data.dto.loan.GetLoanDto
import com.example.h_bank.data.dto.loan.LoanListResponseDto
import com.example.h_bank.data.dto.loan.TariffListResponseDto
import com.example.h_bank.data.network.LoanApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoanRemoteDataSource(
    private val api: LoanApi
) {
    suspend fun getTariffList(pageNumber: Int, pageSize: Int): RequestResult<TariffListResponseDto> = withContext(Dispatchers.IO) {
        runResultCatching {
            api.getTariffList(pageNumber, pageSize)
        }
    }

    suspend fun getUserLoans(userId: String, pageNumber: Int, pageSize: Int): RequestResult<LoanListResponseDto> = withContext(Dispatchers.IO) {
        runResultCatching {
            api.getUserLoans(userId, pageNumber, pageSize)
        }
    }

    suspend fun getLoan(request: GetLoanDto): RequestResult<Unit> = withContext(Dispatchers.IO) {
        runResultCatching {
            api.getLoan(request)
        }
    }

    suspend fun getCreditRating(userId: String): RequestResult<CreditRatingDto> = withContext(
        Dispatchers.IO) {
        runResultCatching {
            api.getCreditRating(userId)
        }
    }
}