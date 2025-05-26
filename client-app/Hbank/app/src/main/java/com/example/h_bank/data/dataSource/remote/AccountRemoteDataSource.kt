package com.example.h_bank.data.dataSource.remote

import com.example.h_bank.data.dto.AccountDto
import com.example.h_bank.data.dto.CurrencyDto
import com.example.h_bank.data.network.AccountApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRemoteDataSource(
    private val api: AccountApi
) {
    suspend fun getUserAccounts(userId: String): RequestResult<List<AccountDto>> =
        withContext(Dispatchers.IO) {
            runResultCatching(infiniteRetry = true) {
                api.getUserAccounts(userId)
            }
        }

    suspend fun openAccount(currency: CurrencyDto): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            runResultCatching {
                api.openAccount(currency)
            }
        }

    suspend fun closeAccount(accountId: String): RequestResult<Unit> = withContext(Dispatchers.IO) {
        runResultCatching {
            api.closeAccount(accountId)
        }
    }

    suspend fun getAccountIdByNumber(accountNumber: String): RequestResult<String> =
        withContext(Dispatchers.IO) {
            runResultCatching {
                api.getAccountIdByNumber(accountNumber)
            }
        }
}