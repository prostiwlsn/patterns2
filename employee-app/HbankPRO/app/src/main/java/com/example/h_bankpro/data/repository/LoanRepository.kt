package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.LoanListResponseDto
import com.example.h_bankpro.data.dto.TariffDto
import com.example.h_bankpro.data.dto.TariffListResponseDto
import com.example.h_bankpro.data.dto.TariffRequestDto
import com.example.h_bankpro.data.network.LoanApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.ILoanRepository

class LoanRepository(
    private val loanApi: LoanApi
) : ILoanRepository {
    override suspend fun createTariff(request: TariffRequestDto): RequestResult<Unit> {
        return runResultCatching { loanApi.createTariff(request) }
    }

    override suspend fun getTariffList(
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<TariffListResponseDto> {
        return runResultCatching { loanApi.getTariffList(pageNumber, pageSize) }
    }

    override suspend fun deleteTariff(tariffId: String): RequestResult<Unit> {
        return runResultCatching { loanApi.deleteTariff(tariffId) }
    }

    override suspend fun updateTariff(
        tariffId: String,
        request: TariffRequestDto
    ): RequestResult<Unit> {
        return runResultCatching { loanApi.updateTariff(tariffId, request) }
    }

    override suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto> {
        return runResultCatching { loanApi.getUserLoans(userId, pageNumber, pageSize) }
    }
}