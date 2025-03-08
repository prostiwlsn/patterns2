package com.example.h_bank.domain.useCase

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAccountRepository

class CloseAccountUseCase(
    private val accountRepository: IAccountRepository,
) {
    suspend operator fun invoke(accountId: String): RequestResult<Unit> {
        return accountRepository.closeAccount(accountId)
    }
}