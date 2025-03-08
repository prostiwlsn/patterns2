package com.example.h_bank.domain.repository.loan

import com.example.h_bank.data.dto.loan.LoanListResponseDto
import com.example.h_bank.data.dto.loan.TariffListResponseDto
import com.example.h_bank.data.utils.RequestResult

interface ILoanRemoteRepository {
    //suspend fun createTariff(request: TariffRequestDto): RequestResult<Unit>
    suspend fun getTariffList(pageNumber: Int, pageSize: Int): RequestResult<TariffListResponseDto>
    /*suspend fun deleteTariff(tariffId: String): RequestResult<Unit>
    suspend fun updateTariff(tariffId: String, request: TariffRequestDto): RequestResult<Unit>
    */
    suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto>
}