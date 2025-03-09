package com.example.h_bank.domain.repository.payment

import com.example.h_bank.domain.entity.filter.OperationFilterEntity
import kotlinx.coroutines.flow.Flow

interface IPaymentStorageRepository {
    fun updateFilters(update: OperationFilterEntity.() -> OperationFilterEntity)

    fun getFilters(): OperationFilterEntity

    fun getFiltersFlow(): Flow<OperationFilterEntity>
}