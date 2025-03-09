package com.example.h_bankpro.domain.useCase.tariff

import com.example.h_bankpro.domain.entity.TariffEntity
import com.example.h_bankpro.domain.repository.ILoanStorageRepository

class UpdateCurrentTariffUseCase(
    private val storageRepository: ILoanStorageRepository,
) {
    operator fun invoke(update: TariffEntity.() -> TariffEntity) =
        storageRepository.updateTariff(update)
}