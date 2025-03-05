package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.data.utils.mapSuccess
import com.example.h_bankpro.domain.model.User
import com.example.h_bankpro.domain.repository.IUserRepository

class GetUserByIdUseCase(
    private val userRepository: IUserRepository,
) {
    suspend operator fun invoke(userId: String): RequestResult<User> {
        return userRepository.getUserById(userId)
            .mapSuccess { userDto -> userDto.toDomain() }
    }
}
