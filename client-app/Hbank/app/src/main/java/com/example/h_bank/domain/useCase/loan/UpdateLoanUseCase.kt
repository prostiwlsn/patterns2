package com.example.h_bank.domain.useCase.loan

import com.example.h_bank.domain.entity.loan.LoanEntity
import com.example.h_bank.domain.repository.loan.ILoanStorageRepository

class UpdateLoanUseCase(
    private val loanStorageRepository: ILoanStorageRepository,
) {
    operator fun invoke(update: LoanEntity.() -> LoanEntity) =
        loanStorageRepository.updateLoan(update)
}