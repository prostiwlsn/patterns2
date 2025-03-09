package com.example.h_bankpro.domain.useCase.tariff

import com.example.h_bankpro.domain.repository.ILoanStorageRepository

class GetTariffFlowUseCase(
    private val storageRepository: ILoanStorageRepository,
) {
    operator fun invoke() = storageRepository.getTariffFlow()
}