package com.example.h_bank.data.dataSource.remote

import com.example.h_bank.data.dto.RefreshRequestDto
import com.example.h_bank.data.dto.TokenDto
import com.example.h_bank.data.network.TokenApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.entity.authorization.TokenEntity
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenRemoteDataSource(
    private val tokenApi: TokenApi,
    private val localRepository: IAuthorizationLocalRepository
) {
    suspend fun refreshToken(refreshToken: String): RequestResult<TokenDto> = withContext(Dispatchers.IO) {
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