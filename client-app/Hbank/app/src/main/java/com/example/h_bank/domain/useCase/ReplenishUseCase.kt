package com.example.h_bank.domain.useCase

import com.example.h_bank.data.OperationType
import com.example.h_bank.data.dto.OperationRquestBody
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IOperationRepository

class ReplenishUseCase(
    private val operationRepository: IOperationRepository,
) {
    suspend operator fun invoke(
        recipientAccountId: String,
        amount: Double
    ): RequestResult<Unit> {
        val request = OperationRquestBody(
            senderAccountId = null,
            recipientAccountId = recipientAccountId,
            amount = amount,
            message = null,
            operationType = OperationType.REPLENISHMENT
        )
        return operationRepository.createOperation(request)
    }
}