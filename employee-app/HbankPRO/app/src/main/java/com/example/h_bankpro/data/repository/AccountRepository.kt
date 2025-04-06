package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dataSource.remote.AccountRemoteDataSource
import com.example.h_bankpro.data.dto.AccountDTO
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IAccountRepository

class AccountRepository(
    private val remoteDataSource: AccountRemoteDataSource
) : IAccountRepository {
    override suspend fun getUserAccounts(userId: String): RequestResult<List<AccountDTO>> {
        return remoteDataSource.getUserAccounts(userId)
    }
}