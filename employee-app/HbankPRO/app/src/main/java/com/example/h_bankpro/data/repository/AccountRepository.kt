package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.AccountDTO
import com.example.h_bankpro.data.network.AccountApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IAccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository(
    private val api: AccountApi,
) : IAccountRepository {
    override suspend fun getUserAccounts(
        userId: String,
    ): RequestResult<List<AccountDTO>> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            api.getUserAccounts(userId)
        }
    }
}