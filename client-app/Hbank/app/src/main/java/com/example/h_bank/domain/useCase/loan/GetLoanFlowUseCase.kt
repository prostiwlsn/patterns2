package com.example.h_bank.domain.useCase.loan

import com.example.h_bank.domain.repository.loan.ILoanStorageRepository

class GetLoanFlowUseCase(
    private val storageRepository: ILoanStorageRepository,
) {
    operator fun invoke() = storageRepository.getLoanFlow()
}