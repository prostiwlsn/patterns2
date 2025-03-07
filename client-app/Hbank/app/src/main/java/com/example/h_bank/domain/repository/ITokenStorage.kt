package com.example.h_bank.domain.repository

import com.example.h_bank.domain.entity.TokenEntity
import kotlinx.coroutines.flow.Flow

interface ITokenStorage {
    fun saveToken(token: TokenEntity)
    fun getTokenFlow(): Flow<TokenEntity>
    fun getTokenState(): TokenEntity
    fun clearToken()
    fun isTokenValid(): Boolean
}