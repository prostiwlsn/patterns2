package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.dto.LoanListResponseDto
import com.example.h_bankpro.data.dto.TariffDto
import com.example.h_bankpro.data.dto.TariffListResponseDto
import com.example.h_bankpro.data.dto.TariffRequestDto
import com.example.h_bankpro.data.utils.RequestResult

interface ILoanRepository {
    suspend fun createTariff(request: TariffRequestDto): RequestResult<TariffDto>
    suspend fun getTariffList(pageNumber: Int, pageSize: Int): RequestResult<TariffListResponseDto>
    suspend fun deleteTariff(tariffId: String): RequestResult<Unit>
    suspend fun updateTariff(tariffId: String, request: TariffRequestDto): RequestResult<TariffDto>
    suspend fun getUserLoans(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): RequestResult<LoanListResponseDto>
}