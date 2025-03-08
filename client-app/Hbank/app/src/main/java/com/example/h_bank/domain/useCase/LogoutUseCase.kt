package com.example.h_bank.domain.useCase

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IAuthorizationRemoteRepository

class LogoutUseCase(
    private val authorizationRepository: IAuthorizationRemoteRepository,
) {
    suspend operator fun invoke(): RequestResult<Unit> {
        return authorizationRepository.logout()
    }
}