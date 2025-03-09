package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.ILoanRepository

class DeleteTariffUseCase(
    private val loanRepository: ILoanRepository
) {
    suspend operator fun invoke(tariffId: String): RequestResult<Unit> {
        return loanRepository.deleteTariff(tariffId)
    }
}