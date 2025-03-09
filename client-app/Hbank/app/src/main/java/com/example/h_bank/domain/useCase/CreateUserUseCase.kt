package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.UserCreationDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IUserRepository


class CreateUserUseCase(
    private val userRepository: IUserRepository,
) {
    suspend operator fun invoke(request: UserCreationDto): RequestResult<Unit> {
        return userRepository.createUser(request)
    }
}