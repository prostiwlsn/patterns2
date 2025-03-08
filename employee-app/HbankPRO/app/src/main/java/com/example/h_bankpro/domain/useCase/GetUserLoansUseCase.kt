package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.data.utils.mapSuccess
import com.example.h_bankpro.domain.model.Loan
import com.example.h_bankpro.domain.model.LoansPageResponse
import com.example.h_bankpro.domain.repository.ILoanRepository

class GetUserLoansUseCase(
    private val loanRepository: ILoanRepository
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