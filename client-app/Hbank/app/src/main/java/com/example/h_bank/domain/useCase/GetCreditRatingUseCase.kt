package com.example.h_bank.domain.useCase

import com.example.h_bank.domain.repository.loan.ILoanRemoteRepository


class GetCreditRatingUseCase(
    private val loanRepository: ILoanRemoteRepository
) {
    suspend operator fun invoke(userId: String) = loanRepository.getCreditRating(userId)
}