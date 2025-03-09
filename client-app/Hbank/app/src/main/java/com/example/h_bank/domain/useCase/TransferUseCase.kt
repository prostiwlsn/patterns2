package com.example.h_bank.domain.useCase

import com.example.h_bank.data.OperationType
import com.example.h_bank.data.dto.OperationRequestBody
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IOperationRepository

class TransferUseCase(
    private val operationRepository: IOperationRepository,
) {
    suspend operator fun invoke(
        senderAccountId: String,
        recipientAccountId: String,
        amount: Double,
        message: String?,
    ): RequestResult<Unit> {
        val request = OperationRequestBody(
            senderAccountId = senderAccountId,
            recipientAccountId = recipientAccountId,
            amount = amount,
            message = message,
            operationType = OperationType.TRANSFER
        )
        return operationRepository.createOperation(request)
    }
}