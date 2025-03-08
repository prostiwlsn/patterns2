package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.TariffRequestDto
import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.data.utils.mapSuccess
import com.example.h_bankpro.domain.model.Tariff
import com.example.h_bankpro.domain.repository.ILoanRepository

class UpdateTariffUseCase(
    private val loanRepository: ILoanRepository
) {
    suspend operator fun invoke(
        tariffId: String,
        name: String,
        ratePercent: Double,
        description: String
    ): RequestResult<Tariff> {
        val request = TariffRequestDto(name, ratePercent, description)
        return loanRepository.updateTariff(tariffId, request)
            .mapSuccess { it.toDomain() }
    }
}