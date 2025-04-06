package com.example.h_bankpro.data.dataSource.remote

import com.example.h_bankpro.data.dto.RefreshRequestDto
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.network.TokenApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenRemoteDataSource(
    private val tokenApi: TokenApi
) {
    suspend fun refreshToken(refreshToken: String): RequestResult<TokenDto> =
        withContext(Dispatchers.IO) {
            runResultCatching {
                tokenApi.refreshToken(
                    request = RefreshRequestDto(refreshToken),
                    accessToken = "Bearer $refreshToken"
                )
            }
        }
}