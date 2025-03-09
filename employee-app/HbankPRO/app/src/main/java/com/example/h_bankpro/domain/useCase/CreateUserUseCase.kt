package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.UserCreationDto
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IUserRepository

class CreateUserUseCase(
    private val userRepository: IUserRepository,
) {
    suspend operator fun invoke(request: UserCreationDto): RequestResult<Unit> {
        return userRepository.createUser(request)
    }
}