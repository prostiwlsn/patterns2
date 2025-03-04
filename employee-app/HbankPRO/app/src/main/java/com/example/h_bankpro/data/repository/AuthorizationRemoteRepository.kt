package com.example.h_bankpro.data.repository

import com.example.h_bankpro.data.dto.RegisterDto
import com.example.h_bankpro.data.dto.TokenDto
import com.example.h_bankpro.data.mapper.toLoginRequestDto
import com.example.h_bankpro.data.network.AuthorizationApi
import com.example.h_bankpro.data.utils.NetworkUtils
import com.example.h_bankpro.data.utils.NetworkUtils.runResultCatching
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IAuthorizationLocalRepository
import com.example.h_bankpro.domain.repository.IAuthorizationRemoteRepository

class AuthorizationRemoteRepository(
    private val storageRepository: IAuthorizationLocalRepository,
    private val api: AuthorizationApi,
): IAuthorizationRemoteRepository {
    override suspend fun login(): RequestResult<TokenDto> {
        val loginRequest = storageRepository.getCredentialsState().toLoginRequestDto()

        return runResultCatching {
            api.login(loginRequest)
        }
    }

    override suspend fun register(request: RegisterDto) = runResultCatching {
        api.register(request)
    }
}