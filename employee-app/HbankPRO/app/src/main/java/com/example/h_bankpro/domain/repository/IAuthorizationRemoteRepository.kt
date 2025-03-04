package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.utils.RequestResult

interface IAuthorizationRemoteRepository {
    suspend fun login(): RequestResult<TokenDto>
}