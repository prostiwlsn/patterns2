package com.example.h_bank.domain.useCase

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IUserRepository

class UnblockUserUseCase(
    private val userRepository: IUserRepository,
) {
    suspend operator fun invoke(userId: String): RequestResult<Unit> {
        return userRepository.unblockUser(userId)
    }
}