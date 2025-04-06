package com.example.h_bankpro.data.dataSource.remote

import com.example.h_bankpro.data.dto.RegisterDto
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.network.LogoutApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorizationRemoteDataSource(
    private val logoutApi: LogoutApi
) {
    suspend fun logout(): RequestResult<Unit> = withContext(Dispatchers.IO) {
        runResultCatching { logoutApi.logout() }
    }
}