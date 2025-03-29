package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.AccountDto
import com.example.h_bank.data.dto.CurrencyDto
import com.example.h_bank.data.network.AccountApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository(
    private val api: AccountApi,
) : IAccountRepository {
    override suspend fun getUserAccounts(
        userId: String,
    ): RequestResult<List<AccountDto>> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            api.getUserAccounts(userId)
        }
    }

    override suspend fun openAccount(
        currency: CurrencyDto,
    ): RequestResult<Unit> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            api.openAccount(currency)
        }
    }

    override suspend fun closeAccount(accountId: String): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching {
                api.closeAccount(accountId)
            }
        }

    override suspend fun getAccountIdByNumber(accountNumber: String): RequestResult<String> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching {
                api.getAccountIdByNumber(accountNumber)
            }
        }
}