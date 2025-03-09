package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.toDomain
import com.example.h_bank.data.utils.NetworkUtils.onSuccess
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.entity.authorization.UserEntity
import com.example.h_bank.domain.repository.authorization.IAuthorizationLocalRepository
import com.example.h_bank.domain.repository.IUserRepository


class GetCurrentUserUseCase(
    private val userRepository: IUserRepository,
    private val storageRepository: IAuthorizationLocalRepository,
) {
    suspend operator fun invoke(): RequestResult<UserEntity> {
        return userRepository.getCurrentUser()
            .mapSuccess { userDto -> userDto.toDomain() }
            .onSuccess {
                storageRepository.setUserId(it.data.id)
            }
    }
}
