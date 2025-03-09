package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.domain.entity.TariffEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

interface ILoanStorageRepository {
    fun updateTariff(update: TariffEntity.() -> TariffEntity)

    fun getTariffFlow(): Flow<TariffEntity>

    fun getTariffState(): TariffEntity
}