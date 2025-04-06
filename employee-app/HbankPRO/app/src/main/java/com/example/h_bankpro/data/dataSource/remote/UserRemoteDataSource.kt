package com.example.h_bankpro.data.dataSource.remote

import com.example.h_bankpro.data.dto.UserCreationDto
import com.example.h_bankpro.data.dto.UserDto
import com.example.h_bankpro.data.network.UserApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRemoteDataSource(
    private val api: UserApi
) {
    suspend fun getUsers(): RequestResult<List<UserDto>> = withContext(Dispatchers.IO) {
        runResultCatching { api.getUsers() }
    }

    suspend fun getCurrentUser(): RequestResult<UserDto> = withContext(Dispatchers.IO) {
        runResultCatching { api.getCurrentUser() }
    }

    suspend fun getUserById(userId: String): RequestResult<UserDto> = withContext(Dispatchers.IO) {
        runResultCatching { api.getUserById(userId) }
    }

    suspend fun blockUser(userId: String): RequestResult<Unit> = withContext(Dispatchers.IO) {
        runResultCatching { api.blockUser(userId) }
    }

    suspend fun unblockUser(userId: String): RequestResult<Unit> = withContext(Dispatchers.IO) {
        runResultCatching { api.unblockUser(userId) }
    }

    suspend fun createUser(request: UserCreationDto): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            runResultCatching { api.createUser(request) }
        }
}