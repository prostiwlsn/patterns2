package com.example.h_bank.data.dataSource.remote

import com.example.h_bank.data.FcmTokenRequest
import com.example.h_bank.data.dto.UserCreationDto
import com.example.h_bank.data.dto.UserDto
import com.example.h_bank.data.network.UserApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRemoteDataSource(
    private val api: UserApi
) {
    suspend fun getUsers(): RequestResult<List<UserDto>> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching { api.getUsers() }
    }

    suspend fun getCurrentUser(): RequestResult<UserDto> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching(infiniteRetry = true) { api.getCurrentUser() }
    }

    suspend fun getUserById(userId: String): RequestResult<UserDto> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching { api.getUserById(userId) }
    }

    suspend fun blockUser(userId: String): RequestResult<Unit> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching { api.blockUser(userId) }
    }

    suspend fun unblockUser(userId: String): RequestResult<Unit> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching { api.unblockUser(userId) }
    }

    suspend fun createUser(request: UserCreationDto): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching { api.createUser(request) }
        }
}