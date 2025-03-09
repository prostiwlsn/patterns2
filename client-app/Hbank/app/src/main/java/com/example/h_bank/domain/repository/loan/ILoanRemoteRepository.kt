package com.example.h_bank.domain.repository.loan

import com.example.h_bank.data.dto.loan.GetLoanDto
import com.example.h_bank.data.dto.loan.LoanListResponseDto
import com.example.h_bank.data.dto.loan.TariffListResponseDto
import com.example.h_bank.data.utils.RequestResult

interface ILoanRemoteRepository {
    suspend fun getTariffList(pageNumber: Int, pageSize: Int): RequestResult<TariffListResponseDto>

    suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto>

    suspend fun getLoan(
        request: GetLoanDto,
    ): RequestResult<Unit>
}