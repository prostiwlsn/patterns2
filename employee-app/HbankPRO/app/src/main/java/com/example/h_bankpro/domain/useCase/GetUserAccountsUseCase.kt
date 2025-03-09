package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.Account
import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.data.utils.mapSuccess
import com.example.h_bankpro.domain.repository.IAccountRepository

class GetUserAccountsUseCase(
    private val accountRepository: IAccountRepository,
) {
    suspend operator fun invoke(userId: String): RequestResult<List<Account>> {
        return accountRepository.getUserAccounts(userId)
            .mapSuccess { accountsDto -> accountsDto.map { it.toDomain() } }
    }
}
