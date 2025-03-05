package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.domain.entity.TokenEntity
import com.example.h_bankpro.domain.repository.ITokenStorage

class SaveTokenUseCase(
    private val tokenStorage: ITokenStorage
) {
    operator fun invoke(token: TokenEntity) {
        tokenStorage.saveToken(token)
    }
}