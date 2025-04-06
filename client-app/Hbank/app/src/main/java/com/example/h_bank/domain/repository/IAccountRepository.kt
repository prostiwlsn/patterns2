package com.example.h_bank.domain.repository

import com.example.h_bank.data.dto.AccountDto
import com.example.h_bank.data.dto.CurrencyDto
import com.example.h_bank.data.utils.RequestResult


interface IAccountRepository {
    suspend fun getUserAccounts(userId: String): RequestResult<List<AccountDto>>
    suspend fun openAccount(currency: CurrencyDto): RequestResult<Unit>
    suspend fun closeAccount(accountId: String): RequestResult<Unit>
    suspend fun getAccountIdByNumber(accountNumber: String): RequestResult<String>
}