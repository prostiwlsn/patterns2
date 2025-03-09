package com.example.h_bank.domain.useCase.filter

import com.example.h_bank.domain.entity.filter.OperationFilterEntity
import com.example.h_bank.domain.repository.payment.IPaymentStorageRepository

class UpdateOperationsFilterUseCase(
    private val storageRepository: IPaymentStorageRepository,
) {
    operator fun invoke(update: OperationFilterEntity.() -> OperationFilterEntity) =
        storageRepository.updateFilters(update)
}