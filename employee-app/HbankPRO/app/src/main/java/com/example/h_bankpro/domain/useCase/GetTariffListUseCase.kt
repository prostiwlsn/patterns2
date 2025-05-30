package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.data.utils.mapSuccess
import com.example.h_bankpro.domain.model.LoansPageResponse
import com.example.h_bankpro.domain.model.Tariff
import com.example.h_bankpro.domain.repository.ILoanRepository

class GetTariffListUseCase(
    private val loanRepository: ILoanRepository
) {
    suspend operator fun invoke(
        pageNumber: Int = 0,
        pageSize: Int = 10
    ): RequestResult<LoansPageResponse<Tariff>> {
        return loanRepository.getTariffList(pageNumber, pageSize)
            .mapSuccess { it.toDomain() }
    }
}