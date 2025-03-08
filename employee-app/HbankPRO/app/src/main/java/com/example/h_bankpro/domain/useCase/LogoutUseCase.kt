package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository

class LogoutUseCase(
    private val authorizationRepository: IAuthorizationRemoteRepository,
) {
    suspend operator fun invoke(): RequestResult<Unit> {
        return authorizationRepository.logout()
    }
}