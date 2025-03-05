package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.UserDto
import com.example.h_bankpro.data.network.UserApi
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IUserRepository

class UserRepository(
    private val api: UserApi,
) : IUserRepository {
    override suspend fun getUsers(): RequestResult<List<UserDto>> {
        return runResultCatching {
            api.getUsers()
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
}