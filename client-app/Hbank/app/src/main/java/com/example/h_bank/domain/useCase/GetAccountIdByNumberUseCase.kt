package com.example.h_bank.domain.useCase

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAccountRepository

class GetAccountIdByNumberUseCase(
    private val accountRepository: IAccountRepository,
) {
    suspend operator fun invoke(accountNumber: String): RequestResult<String> {
        return accountRepository.getAccountIdByNumber(accountNumber)
    }
}