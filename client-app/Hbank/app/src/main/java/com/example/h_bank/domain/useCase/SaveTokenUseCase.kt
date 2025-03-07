package com.example.h_bank.domain.useCase

import com.example.h_bank.domain.entity.TokenEntity
import com.example.h_bank.domain.repository.ITokenStorage

class SaveTokenUseCase(
    private val tokenStorage: ITokenStorage
) {
    operator fun invoke(token: TokenEntity) {
        tokenStorage.saveToken(token)
    }
}