package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.AccountDTO
import com.example.h_bank.data.network.AccountApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAccountRepository

class AccountRepository(
    private val api: AccountApi,
) : IAccountRepository {
    override suspend fun getUserAccounts(
        userId: String,
    ): RequestResult<List<AccountDTO>> {
        return runResultCatching {
            api.getUserAccounts(userId)
        }
    }
}