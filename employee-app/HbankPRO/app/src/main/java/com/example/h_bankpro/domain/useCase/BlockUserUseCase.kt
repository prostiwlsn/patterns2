package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IUserRepository

class BlockUserUseCase(
    private val userRepository: IUserRepository,
) {
    suspend operator fun invoke(userId: String): RequestResult<Unit> {
        return userRepository.blockUser(userId)
    }
}