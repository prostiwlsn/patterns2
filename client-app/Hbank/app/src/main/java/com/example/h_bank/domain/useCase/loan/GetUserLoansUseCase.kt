package com.example.h_bank.domain.useCase.loan

import com.example.h_bank.data.Loan
import com.example.h_bank.data.dto.loan.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.entity.loan.LoansPageResponse
import com.example.h_bank.domain.repository.loan.ILoanRemoteRepository

class GetUserLoansUseCase(
    private val loanRepository: ILoanRemoteRepository
) {
    suspend operator fun invoke(
        userId: String,
        pageNumber: Int = 1,
        pageSize: Int = 10
    ): RequestResult<LoansPageResponse<Loan>> {
        return loanRepository.getUserLoans(userId, pageNumber, pageSize)
            .mapSuccess { it.toDomain() }
    }
}