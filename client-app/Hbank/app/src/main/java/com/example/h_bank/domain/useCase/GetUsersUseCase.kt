package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.entity.authorization.UserEntity
import com.example.h_bank.domain.repository.IUserRepository

class GetUsersUseCase(
    private val userRepository: IUserRepository,
) {
    suspend operator fun invoke(): RequestResult<List<UserEntity>> {
        return userRepository.getUsers()
            .mapSuccess { usersDto -> usersDto.map { it.toDomain() } }
    }
}
