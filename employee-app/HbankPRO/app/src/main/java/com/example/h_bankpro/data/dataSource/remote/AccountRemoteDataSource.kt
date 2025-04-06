package com.example.h_bankpro.data.dataSource.remote

import com.example.h_bankpro.data.dto.AccountDTO
import com.example.h_bankpro.data.network.AccountApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRemoteDataSource(
    private val api: AccountApi
) {
    suspend fun getUserAccounts(userId: String): RequestResult<List<AccountDTO>> =
        withContext(Dispatchers.IO) {
            runResultCatching { api.getUserAccounts(userId) }
        }
}