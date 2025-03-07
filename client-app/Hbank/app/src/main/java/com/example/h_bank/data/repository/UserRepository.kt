package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.UserCreationDto
import com.example.h_bank.data.dto.UserDto
import com.example.h_bank.data.network.UserApi
import com.example.h_bank.data.utils.NetworkUtils.runResultCatching
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IUserRepository

class UserRepository(
    private val api: UserApi,
) : IUserRepository {
    override suspend fun getUsers(): RequestResult<List<UserDto>> {
        return runResultCatching {
            api.getUsers()
        }
    }

    override suspend fun getCurrentUser(): RequestResult<UserDto> {
        return runResultCatching {
            api.getCurrentUser()
        }
    }

    override suspend fun getUserById(userId: String): RequestResult<UserDto> {
        return runResultCatching {
            api.getUserById(userId)
        }
    }

    override suspend fun blockUser(userId: String): RequestResult<Unit> {
        return runResultCatching {
            api.blockUser(userId)
        }
    }

    override suspend fun unblockUser(userId: String): RequestResult<Unit> {
        return runResultCatching {
            api.unblockUser(userId)
        }
    }

    override suspend fun createUser(request: UserCreationDto): RequestResult<Unit> {
        return runResultCatching {
            api.createUser(request)
        }
    }
}