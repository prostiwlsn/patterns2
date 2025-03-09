package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.entity.TokenEntity

interface ITokenRepository {
    suspend fun getToken(): TokenEntity?
    suspend fun saveToken(token: TokenEntity)
    suspend fun clearToken()
    suspend fun refreshToken(refreshToken: String): RequestResult<TokenDto>
}