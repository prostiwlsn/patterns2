package com.example.h_bank.domain.useCase.authorization

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.authorization.IAuthorizationRemoteRepository

class LogoutUseCase(
    private val authorizationRepository: IAuthorizationRemoteRepository,
) {
    suspend operator fun invoke(): RequestResult<Unit> {
        return authorizationRepository.logout()
    }
}