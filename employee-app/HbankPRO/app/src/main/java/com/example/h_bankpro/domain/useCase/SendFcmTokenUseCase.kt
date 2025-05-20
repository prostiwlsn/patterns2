package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.domain.repository.IOperationRepository
import kotlinx.coroutines.delay
import java.io.IOException

class SendFcmTokenUseCase(
    private val operationRepository: IOperationRepository
) {
    suspend operator fun invoke(
        userId: String,
        isManager: Boolean,
        fcmToken: String
    ): RequestResult<Unit> {
        val maxAttempts = 3
        var attempt = 1
        var delayMs = 1000L

        while (attempt <= maxAttempts) {
            try {
                operationRepository.sendFcmToken(userId, isManager, fcmToken)
                return RequestResult.Success(Unit)
            } catch (e: IOException) {
                if (attempt == maxAttempts) {
                    return RequestResult.NoInternetConnection()
                }
            } catch (e: Exception) {
                val message = e.message ?: "Unknown error"
                if (message.contains("405") || attempt == maxAttempts) {
                    return RequestResult.Error(code = -1, message = message)
                }
            }
            delay(delayMs)
            attempt++
            delayMs *= 2
        }
        return RequestResult.Error(code = -1, message = "Failed after $maxAttempts attempts")
    }
}