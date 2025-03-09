package com.example.h_bank.domain.useCase

import com.example.h_bank.data.Account
import com.example.h_bank.data.dto.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.repository.IAccountRepository

class GetUserAccountsUseCase(
    private val accountRepository: IAccountRepository,
) {
    suspend operator fun invoke(userId: String): RequestResult<List<Account>> {
        return accountRepository.getUserAccounts(userId)
            .mapSuccess { accountsDto -> accountsDto.map { it.toDomain() } }
    }
}
