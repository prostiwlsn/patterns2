package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.RefreshRequestDto
import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.network.TokenApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.authorization.TokenEntity
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.authorization.ITokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenRepository(
    private val localRepository: IAuthorizationLocalRepository,
    private val tokenApi: TokenApi
) : ITokenRepository {
    override suspend fun getToken(): TokenEntity? = withContext(Dispatchers.IO) {
        return@withContext localRepository.getToken()
    }

    override suspend fun saveToken(token: TokenEntity) = withContext(Dispatchers.IO) {
        localRepository.saveToken(token)
    }

    override suspend fun clearToken() = withContext(Dispatchers.IO) {
        localRepository.clearToken()
    }

    override suspend fun refreshToken(refreshToken: String): RequestResult<TokenDto> =
        withContext(Dispatchers.IO) {
            val result = runResultCatching {
                tokenApi.refreshToken(
                    request = RefreshRequestDto(refreshToken),
                    accessToken = "Bearer $refreshToken"
                )
            }
            return@withContext when (result) {
                is RequestResult.Success -> {
                    localRepository.saveToken(TokenEntity.fromTokenDto(result.data))
                    result
                }

                is RequestResult.Error -> result
                is RequestResult.NoInternetConnection -> result
            }
        }
}