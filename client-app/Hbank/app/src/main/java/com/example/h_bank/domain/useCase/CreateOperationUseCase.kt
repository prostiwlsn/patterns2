package com.example.h_bank.domain.useCase

import com.example.h_bank.data.OperationType
import com.example.h_bank.data.dto.OperationCreationRequestDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IOperationRepository

class CreateOperationUseCase(
    private val operationRepository: IOperationRepository,
) {
    suspend operator fun invoke(
        senderAccountId: String?,
        recipientAccountId: String,
        amount: Double,
        message: String?,
        operationType: OperationType
    ): RequestResult<Unit> {
        val request = OperationCreationRequestDto(
            senderAccountId = senderAccountId,
            recipientAccountId = recipientAccountId,
            amount = amount,
            message = message,
            operationType = operationType
        )
        return operationRepository.createOperation(request)
    }
}