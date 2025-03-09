package com.example.h_bank.data.repository.payment

import com.example.h_bank.domain.entity.filter.OperationFilterEntity
import com.example.h_bank.domain.repository.payment.IPaymentStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class PaymentStorageRepository: IPaymentStorageRepository {
    private val filters = MutableStateFlow(OperationFilterEntity())

    override fun updateFilters(update: OperationFilterEntity.() -> OperationFilterEntity) {
        filters.update { filters.value.update() }
    }

    override fun getFilters() = filters.value

    override fun getFiltersFlow() = filters
}