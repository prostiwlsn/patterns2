package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.RefreshRequestDto
import com.example.h_bankpro.data.dto.RegisterDto
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.mapper.toLoginRequestDto
import com.example.h_bankpro.data.network.AuthorizationApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.entity.TokenEntity
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.ITokenStorage

class AuthorizationRemoteRepository(
    private val storageRepository: IAuthorizationLocalRepository,
    private val tokenStorage: ITokenStorage,
    private val api: AuthorizationApi,
) : IAuthorizationRemoteRepository {
    override suspend fun login(): RequestResult<TokenDto> {
        val loginRequest = storageRepository.getCredentialsState().toLoginRequestDto()

        val result = runResultCatching {
            api.login(loginRequest)
        }

        return when (result) {
            is RequestResult.Success -> {
                tokenStorage.saveToken(
                    TokenEntity(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken,
                        expiresAt = result.data.expiresIn?.let { System.currentTimeMillis() + it * 1000L }
                    )
                )
                result
            }

            is RequestResult.Error -> result
            is RequestResult.NoInternetConnection -> result
        }
    }

    override suspend fun register(request: RegisterDto): RequestResult<TokenDto> {
        val result = runResultCatching {
            api.register(request)
        }

        return when (result) {
            is RequestResult.Success -> {
                tokenStorage.saveToken(
                    TokenEntity(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken,
                        expiresAt = result.data.expiresIn?.let { System.currentTimeMillis() + it * 1000L }
                    )
                )
                result
            }

            is RequestResult.Error -> result
            is RequestResult.NoInternetConnection -> result
        }
    }

    override suspend fun refreshToken(request: RefreshRequestDto): RequestResult<TokenDto> {
        val result = runResultCatching { api.refreshToken(request) }
        return when (result) {
            is RequestResult.Success -> {
                tokenStorage.saveToken(
                    TokenEntity(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken,
                        expiresAt = result.data.expiresIn?.let { System.currentTimeMillis() + it * 1000L }
                    )
                )
                result
            }
            else -> result
        }
    }
}