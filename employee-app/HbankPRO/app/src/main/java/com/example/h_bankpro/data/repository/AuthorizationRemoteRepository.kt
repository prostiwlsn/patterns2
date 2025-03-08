package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.RegisterDto
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.mapper.toLoginRequestDto
import com.example.h_bankpro.data.network.AuthorizationApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.entity.TokenEntity
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.ITokenRepository

class AuthorizationRemoteRepository(
    private val storageRepository: IAuthorizationLocalRepository,
    private val api: AuthorizationApi,
    private val tokenRepository: ITokenRepository
) : IAuthorizationRemoteRepository {
    override suspend fun login(): RequestResult<TokenDto> {
        val loginRequest = storageRepository.getCredentialsState().toLoginRequestDto()

        val result = runResultCatching {
            api.login(loginRequest)
        }

        return when (result) {
            is RequestResult.Success -> {
                tokenRepository.saveToken(TokenEntity.fromTokenDto(result.data))
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
                tokenRepository.saveToken(TokenEntity.fromTokenDto(result.data))
                result
            }

            is RequestResult.Error -> result
            is RequestResult.NoInternetConnection -> result
        }
    }
}