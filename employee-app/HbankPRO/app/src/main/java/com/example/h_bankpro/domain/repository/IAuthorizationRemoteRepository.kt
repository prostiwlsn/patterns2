package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.dto.TokenDto

interface IAuthorizationRemoteRepository {
    suspend fun login(): TokenDto
}