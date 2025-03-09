package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.TariffRequestDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.ILoanRepository

class CreateTariffUseCase(
    private val loanRepository: ILoanRepository
) {
    suspend operator fun invoke(
        name: String,
        ratePercent: Double,
        description: String
    ): RequestResult<Unit> {
        val request = TariffRequestDto(name, ratePercent, description)
        return loanRepository.createTariff(request)
    }
}