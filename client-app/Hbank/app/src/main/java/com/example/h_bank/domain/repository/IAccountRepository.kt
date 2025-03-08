package com.example.h_bank.domain.repository

import com.example.h_bank.data.dto.AccountDto
import com.example.h_bank.data.utils.RequestResult


interface IAccountRepository {
    suspend fun getUserAccounts(userId: String): RequestResult<List<AccountDto>>
}