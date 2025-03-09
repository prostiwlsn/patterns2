package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.AccountDto
import com.example.h_bank.data.network.AccountApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAccountRepository

class AccountRepository(
    private val api: AccountApi,
) : IAccountRepository {
    override suspend fun getUserAccounts(
        userId: String,
    ): RequestResult<List<AccountDto>> {
        return runResultCatching {
            api.getUserAccounts(userId)
        }
    }

    override suspend fun openAccount(): RequestResult<Unit> {
        return runResultCatching {
            api.openAccount()
        }
    }

    override suspend fun closeAccount(accountId: String): RequestResult<Unit> {
        return runResultCatching {
            api.closeAccount(accountId)
        }
    }

    override suspend fun getAccountIdByNumber(accountNumber: String): RequestResult<String> {
        return runResultCatching {
            api.getAccountIdByNumber(accountNumber)
        }
    }
}