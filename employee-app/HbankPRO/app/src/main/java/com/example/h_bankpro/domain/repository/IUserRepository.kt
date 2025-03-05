package com.example.h_bankpro.domain.repository

import com.example.h_bankpro.data.dto.UserDto
import com.example.h_bankpro.data.utils.RequestResult

interface IUserRepository {
    suspend fun getUsers(): RequestResult<List<UserDto>>
    suspend fun getUserById(userId: String): RequestResult<UserDto>
    suspend fun blockUser(userId: String): RequestResult<Unit>
    suspend fun unblockUser(userId: String): RequestResult<Unit>
}