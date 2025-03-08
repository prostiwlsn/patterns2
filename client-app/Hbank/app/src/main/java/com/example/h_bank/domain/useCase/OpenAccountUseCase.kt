package com.example.h_bank.domain.useCase

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAccountRepository

class OpenAccountUseCase(
    private val accountRepository: IAccountRepository,
) {
    suspend operator fun invoke(): RequestResult<Unit> {
        return accountRepository.openAccount()
    }
}