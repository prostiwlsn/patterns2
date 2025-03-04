package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.mapper.toLoginRequestDto
import com.example.h_bankpro.data.network.AuthorizationApi
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository

class AuthorizationRemoteRepository(
    private val storageRepository: IAuthorizationLocalRepository,
    private val api: AuthorizationApi,
): IAuthorizationRemoteRepository {
    override suspend fun login(): TokenDto {
        val loginRequest = storageRepository.getCredentialsState().toLoginRequestDto()
        return api.login(loginRequest)
    }

}