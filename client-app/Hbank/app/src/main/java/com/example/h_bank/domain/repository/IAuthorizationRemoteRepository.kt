package com.example.h_bank.domain.repository

import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.utils.RequestResult

interface IAuthorizationRemoteRepository {
    suspend fun login(): RequestResult<TokenDto>
    suspend fun register(request: RegisterDto): RequestResult<TokenDto>
    suspend fun refreshToken(request: RefreshRequestDto): RequestResult<TokenDto>
}