package com.example.h_bank.data.repository

import com.example.h_bank.data.dto.UserCreationDto
import com.example.h_bank.data.dto.UserDto
import com.example.h_bank.data.dataSource.remote.UserRemoteDataSource
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IUserRepository

class UserRepository(
    private val remoteDataSource: UserRemoteDataSource
) : IUserRepository {
    override suspend fun getUsers(): RequestResult<List<UserDto>> {
        return remoteDataSource.getUsers()
    }

    override suspend fun getCurrentUser(): RequestResult<UserDto> {
        return remoteDataSource.getCurrentUser()
    }

    override suspend fun getUserById(userId: String): RequestResult<UserDto> {
        return remoteDataSource.getUserById(userId)
    }

    override suspend fun blockUser(userId: String): RequestResult<Unit> {
        return remoteDataSource.blockUser(userId)
    }

    override suspend fun unblockUser(userId: String): RequestResult<Unit> {
        return remoteDataSource.unblockUser(userId)
    }

    override suspend fun createUser(request: UserCreationDto): RequestResult<Unit> {
        return remoteDataSource.createUser(request)
    }
}