package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dataSource.remote.UserRemoteDataSource
import com.example.h_bankpro.data.dto.UserCreationDto
import com.example.h_bankpro.data.dto.UserDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IUserRepository

class UserRepository(
    private val remoteDataSource: UserRemoteDataSource
) : IUserRepository {
    override suspend fun getUsers(): RequestResult<List<UserDto>> = remoteDataSource.getUsers()

    override suspend fun getCurrentUser(): RequestResult<UserDto> = remoteDataSource.getCurrentUser()

    override suspend fun getUserById(userId: String): RequestResult<UserDto> =
        remoteDataSource.getUserById(userId)

    override suspend fun blockUser(userId: String): RequestResult<Unit> =
        remoteDataSource.blockUser(userId)

    override suspend fun unblockUser(userId: String): RequestResult<Unit> =
        remoteDataSource.unblockUser(userId)

    override suspend fun createUser(request: UserCreationDto): RequestResult<Unit> =
        remoteDataSource.createUser(request)
}