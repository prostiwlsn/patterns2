package com.example.h_bank.data.repository.loan

import com.example.h_bank.data.Loan
import com.example.h_bank.domain.entity.loan.LoanEntity
import com.example.h_bank.domain.repository.loan.ILoanStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LoanStorageRepository: ILoanStorageRepository{
    private val currentLoan = MutableStateFlow(LoanEntity())

    override fun updateLoan(update: LoanEntity.() -> LoanEntity) {
        currentLoan.update{ it.update() }
    }

    override fun getLoanFlow(): Flow<LoanEntity> = currentLoan

    override fun getLoanState() = currentLoan.value
}