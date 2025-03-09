package com.example.h_bankpro.data.repository

import com.example.h_bankpro.domain.entity.TariffEntity
import com.example.h_bankpro.domain.repository.ILoanStorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LoanStorageRepository: ILoanStorageRepository {
    private val tariff = MutableStateFlow(TariffEntity())

    override fun updateTariff(update: TariffEntity.() -> TariffEntity) {
        tariff.update { tariff.value.update() }
    }

    override fun getTariffFlow() = tariff

    override fun getTariffState() = tariff.value
}