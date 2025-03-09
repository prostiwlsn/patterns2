package com.example.h_bank.data.repository.authorization

import com.example.h_bank.data.dto.RegisterDto
import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.mapper.toLoginRequestDto
import com.example.h_bank.data.network.AuthorizationApi
import com.example.h_bank.data.network.LogoutApi
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.authorization.TokenEntity
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.authorization.IAuthorizationRemoteRepository
import com.example.h_bank.domain.repository.authorization.ITokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorizationRemoteRepository(
    private val storageRepository: IAuthorizationLocalRepository,
    private val authApi: AuthorizationApi,
    private val logoutApi: LogoutApi,
    private val tokenRepository: ITokenRepository
) : IAuthorizationRemoteRepository {
    override suspend fun login(): RequestResult<TokenDto> = withContext(Dispatchers.IO) {
        val loginRequest = storageRepository.getCredentialsState().toLoginRequestDto()

        val result = runResultCatching {
            authApi.login(loginRequest)
        }

        return@withContext when (result) {
            is RequestResult.Success -> {
                tokenRepository.saveToken(TokenEntity.fromTokenDto(result.data))
                result
            }

            is RequestResult.Error -> result
            is RequestResult.NoInternetConnection -> result
        }
    }

    override suspend fun register(request: RegisterDto): RequestResult<TokenDto> =
        withContext(Dispatchers.IO) {
            val result = runResultCatching {
                authApi.register(request)
            }

            return@withContext when (result) {
                is RequestResult.Success -> {
                    tokenRepository.saveToken(TokenEntity.fromTokenDto(result.data))
                    result
                }

                is RequestResult.Error -> result
                is RequestResult.NoInternetConnection -> result
            }
        }

    override suspend fun logout(): RequestResult<Unit> =  withContext(Dispatchers.IO) {
        val result = runResultCatching {
            logoutApi.logout()
        }.onSuccess {
            tokenRepository.clearToken()
        }

        return@withContext result
    }
}