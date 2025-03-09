package com.example.h_bank.domain.useCase.loan

import com.example.h_bank.data.dto.loan.GetLoanDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.loan.ILoanRemoteRepository
import com.example.h_bank.domain.repository.loan.ILoanStorageRepository

class GetLoanUseCase(
    private val repository: ILoanRemoteRepository,
    private val userRepository: IAuthorizationLocalRepository,
    private val storageRepository: ILoanStorageRepository
    ) {
    suspend operator fun invoke(
        tariffId: String,
        accountId: String,
    ): RequestResult<Unit> {
        val userId = userRepository.getUserId()
        val loanRequest = storageRepository.getLoanState()

        return repository.getLoan(
            GetLoanDto(
                tariffId = tariffId,
                userId = userId.orEmpty(),
                accountId = accountId,
                durationInYears = loanRequest.duration ?: 0,
                amount = loanRequest.amount ?: 0,
            )
        )
    }
}