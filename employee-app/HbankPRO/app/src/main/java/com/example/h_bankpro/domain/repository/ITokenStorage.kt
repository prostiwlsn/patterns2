package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.domain.entity.TokenEntity
import kotlinx.coroutines.flow.Flow

interface ITokenStorage {
    fun saveToken(token: TokenEntity)
    fun getTokenFlow(): Flow<TokenEntity>
    fun getTokenState(): TokenEntity
    fun clearToken()
    fun isTokenValid(): Boolean
}