package com.example.h_bank.data.repository.loan

import com.example.h_bank.data.dto.loan.LoanListResponseDto
import com.example.h_bank.data.dto.loan.TariffListResponseDto
import com.example.h_bank.data.network.LoanApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.loan.ILoanRemoteRepository

class LoanRemoteRepository(
    private val loanApi: LoanApi
) : ILoanRemoteRepository {
    /*override suspend fun createTariff(request: TariffRequestDto): RequestResult<Unit> {
        return runResultCatching { loanApi.createTariff(request) }
    }*/

    override suspend fun getTariffList(
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<TariffListResponseDto> {
        return runResultCatching { loanApi.getTariffList(pageNumber, pageSize) }
    }

    /*
            override suspend fun deleteTariff(tariffId: String): RequestResult<Unit> {
                return runResultCatching { loanApi.deleteTariff(tariffId) }
            }

            override suspend fun updateTariff(
                tariffId: String,
                request: TariffRequestDto
            ): RequestResult<Unit> {
                return runResultCatching { loanApi.updateTariff(tariffId, request) }
            }
    */
    override suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto> {
        return runResultCatching { loanApi.getUserLoans(userId, pageNumber, pageSize) }
    }
}