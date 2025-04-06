package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dataSource.remote.LoanRemoteDataSource
import com.example.h_bankpro.data.dto.CreditRatingDto
import com.example.h_bankpro.data.dto.LoanListResponseDto
import com.example.h_bankpro.data.dto.TariffListResponseDto
import com.example.h_bankpro.data.dto.TariffRequestDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.ILoanRepository

class LoanRepository(
    private val remoteDataSource: LoanRemoteDataSource
) : ILoanRepository {
    override suspend fun createTariff(request: TariffRequestDto): RequestResult<Unit> =
        remoteDataSource.createTariff(request)

    override suspend fun getTariffList(
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<TariffListResponseDto> = remoteDataSource.getTariffList(pageNumber, pageSize)

    override suspend fun deleteTariff(tariffId: String): RequestResult<Unit> =
        remoteDataSource.deleteTariff(tariffId)

    override suspend fun updateTariff(
        tariffId: String,
        request: TariffRequestDto
    ): RequestResult<Unit> = remoteDataSource.updateTariff(tariffId, request)

    override suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto> = remoteDataSource.getUserLoans(userId, pageNumber, pageSize)

    override suspend fun getCreditRating(userId: String): RequestResult<CreditRatingDto> =
        remoteDataSource.getCreditRating(userId)
}