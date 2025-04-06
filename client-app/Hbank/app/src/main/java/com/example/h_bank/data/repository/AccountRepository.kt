package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.AccountDto
import com.example.h_bank.data.dto.CurrencyDto
import com.example.h_bank.data.dataSource.remote.AccountRemoteDataSource
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAccountRepository

class AccountRepository(
    private val remoteDataSource: AccountRemoteDataSource
) : IAccountRepository {
    override suspend fun getUserAccounts(
        userId: String,
    ): RequestResult<List<AccountDto>> {
        return remoteDataSource.getUserAccounts(userId)
    }

    override suspend fun openAccount(
        currency: CurrencyDto,
    ): RequestResult<Unit> {
        return remoteDataSource.openAccount(currency)
    }

    override suspend fun closeAccount(accountId: String): RequestResult<Unit> {
        return remoteDataSource.closeAccount(accountId)
    }

    override suspend fun getAccountIdByNumber(accountNumber: String): RequestResult<String> {
        return remoteDataSource.getAccountIdByNumber(accountNumber)
    }
}