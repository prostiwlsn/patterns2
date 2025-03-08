package com.example.h_bank.domain.repository

import TokenEntity
import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.utils.RequestResult

interface ITokenRepository {
    suspend fun getToken(): TokenEntity?
    suspend fun saveToken(token: TokenEntity)
    suspend fun clearToken()
    suspend fun refreshToken(refreshToken: String): RequestResult<TokenDto>
}