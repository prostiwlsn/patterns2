package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.domain.repository.ILoanRepository

class GetCreditRatingUseCase(
    private val loanRepository: ILoanRepository
) {
    suspend operator fun invoke(userId: String) = loanRepository.getCreditRating(userId)
}