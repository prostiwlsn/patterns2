package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.OperationRequestBody
import com.example.h_bank.data.dto.payment.OperationTypeDto
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IOperationRepository

class WithdrawUseCase(
    private val operationRepository: IOperationRepository,
) {
    suspend operator fun invoke(
        senderAccountId: String,
        amount: Double,
    ): RequestResult<Unit> {
        val request = OperationRequestBody(
            senderAccountId = senderAccountId,
            recipientAccountId = senderAccountId,
            amount = amount,
            message = null,
            operationType = OperationTypeDto.WITHDRAWAL
        )
        return operationRepository.createOperation(request)
    }
}