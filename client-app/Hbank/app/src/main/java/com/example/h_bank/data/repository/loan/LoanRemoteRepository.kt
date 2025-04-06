package com.example.h_bank.data.repository.loan

import com.example.h_bank.data.dto.CreditRatingDto
import com.example.h_bank.data.dto.loan.GetLoanDto
import com.example.h_bank.data.dto.loan.LoanListResponseDto
import com.example.h_bank.data.dto.loan.TariffListResponseDto
import com.example.h_bank.data.dataSource.remote.LoanRemoteDataSource
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.loan.ILoanRemoteRepository

class LoanRemoteRepository(
    private val remoteDataSource: LoanRemoteDataSource
) : ILoanRemoteRepository {
    override suspend fun getTariffList(pageNumber: Int, pageSize: Int): RequestResult<TariffListResponseDto> {
        return remoteDataSource.getTariffList(pageNumber, pageSize)
    }

    override suspend fun getUserLoans(userId: String, pageNumber: Int, pageSize: Int): RequestResult<LoanListResponseDto> {
        return remoteDataSource.getUserLoans(userId, pageNumber, pageSize)
    }

    override suspend fun getLoan(request: GetLoanDto): RequestResult<Unit> {
        return remoteDataSource.getLoan(request)
    }

    override suspend fun getCreditRating(userId: String): RequestResult<CreditRatingDto> {
        return remoteDataSource.getCreditRating(userId)
    }
}