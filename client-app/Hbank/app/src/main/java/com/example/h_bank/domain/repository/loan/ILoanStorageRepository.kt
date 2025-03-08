package com.example.h_bank.domain.repository.loan

import com.example.h_bank.domain.entity.loan.LoanEntity
import kotlinx.coroutines.flow.Flow

interface ILoanStorageRepository {
    fun updateLoan(update: LoanEntity.() -> LoanEntity)

    fun getLoanFlow(): Flow<LoanEntity>

    fun getLoanState(): LoanEntity
}