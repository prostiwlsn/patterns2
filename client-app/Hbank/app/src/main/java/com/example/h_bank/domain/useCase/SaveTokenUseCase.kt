package com.example.h_bank.domain.useCase

import com.example.h_bank.domain.entity.authorization.TokenEntity
import com.example.h_bank.domain.repository.authorization.ITokenStorage

class SaveTokenUseCase(
    private val tokenStorage: ITokenStorage
) {
    operator fun invoke(token: TokenEntity) {
        tokenStorage.saveToken(token)
    }
}