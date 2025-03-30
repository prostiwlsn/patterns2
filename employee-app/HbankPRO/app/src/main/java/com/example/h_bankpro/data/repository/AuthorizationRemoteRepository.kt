package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.RegisterDto
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.network.LogoutApi
import com.example.h_bankpro.data.utils.NetworkUtils.onSuccess
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository
import com.example.h_bankpro.domain.repository.ITokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorizationRemoteRepository(
    private val logoutApi: LogoutApi,
    private val tokenRepository: ITokenRepository
) : IAuthorizationRemoteRepository {
    override suspend fun login(): RequestResult<TokenDto> = withContext(Dispatchers.IO) {
        val token = tokenRepository.getToken()
        return@withContext if (token?.accessToken != null) {
            RequestResult.Success(
                TokenDto(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken.orEmpty()
                )
            )
        } else {
            RequestResult.Error(400, "")
        }
    }

    override suspend fun register(request: RegisterDto): RequestResult<TokenDto> =
        withContext(Dispatchers.IO) {
            val token = tokenRepository.getToken()
            return@withContext if (token?.accessToken != null) {
                RequestResult.Success(
                    TokenDto(
                        accessToken = token.accessToken,
                        refreshToken = token.refreshToken.orEmpty()
                    )
                )
            } else {
                RequestResult.Error(400, "")
            }
        }

    override suspend fun logout(): RequestResult<Unit> = withContext(Dispatchers.IO) {
        val result = runResultCatching {
            logoutApi.logout()
        }.onSuccess {
            tokenRepository.clearToken()
        }

        return@withContext result
    }
}