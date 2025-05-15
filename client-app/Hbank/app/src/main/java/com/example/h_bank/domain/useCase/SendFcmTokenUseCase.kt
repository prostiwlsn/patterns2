package com.example.h_bank.domain.useCase

import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IOperationRepository

class SendFcmTokenUseCase(
    private val operationRepository: IOperationRepository
) {
    suspend operator fun invoke(
        userId: String,
        isManager: Boolean,
        fcmToken: String
    ): RequestResult<Unit> {
        return try {
            operationRepository.sendFcmToken(userId, isManager, fcmToken)
            RequestResult.Success(Unit)
        } catch (e: Exception) {
            RequestResult.Error(code = -1, message = e.message ?: "Unknown error")
        }
    }
}