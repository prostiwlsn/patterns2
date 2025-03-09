package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.entity.authorization.UserEntity
import com.example.h_bank.domain.repository.IUserRepository

class GetUserByIdUseCase(
    private val userRepository: IUserRepository,
) {
    suspend operator fun invoke(userId: String): RequestResult<UserEntity> {
        return userRepository.getUserById(userId)
            .mapSuccess { userDto -> userDto.toDomain() }
    }
}
