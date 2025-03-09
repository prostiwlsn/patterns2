package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.UserCreationDto
import com.example.h_bank.data.dto.UserDto
import com.example.h_bank.data.network.UserApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val api: UserApi,
) : IUserRepository {
    override suspend fun getUsers(): RequestResult<List<UserDto>> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            api.getUsers()
        }
    }

    override suspend fun getCurrentUser(): RequestResult<UserDto> = withContext(Dispatchers.IO) {
        return@withContext runResultCatching {
            api.getCurrentUser()
        }
    }

    override suspend fun getUserById(userId: String): RequestResult<UserDto> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching {
                api.getUserById(userId)
            }
        }

    override suspend fun blockUser(userId: String): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching {
                api.blockUser(userId)
            }
        }

    override suspend fun unblockUser(userId: String): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching {
                api.unblockUser(userId)
            }
        }

    override suspend fun createUser(request: UserCreationDto): RequestResult<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext runResultCatching {
                api.createUser(request)
            }
        }
}