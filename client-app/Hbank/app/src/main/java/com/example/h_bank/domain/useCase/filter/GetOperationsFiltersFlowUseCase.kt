package com.example.h_bank.domain.useCase.filter

import com.example.h_bank.domain.repository.payment.IPaymentStorageRepository

class GetOperationsFiltersFlowUseCase(
    private val storageRepository: IPaymentStorageRepository
) {
    operator fun invoke() = storageRepository.getFiltersFlow()
}