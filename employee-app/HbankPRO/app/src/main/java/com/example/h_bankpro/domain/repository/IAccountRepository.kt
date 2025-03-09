package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.dto.AccountDTO
import com.example.h_bankpro.data.utils.RequestResult

interface IAccountRepository {
    suspend fun getUserAccounts(userId: String): RequestResult<List<AccountDTO>>
}