package com.example.h_bank.domain.useCase

import com.example.h_bank.data.OperationType
import com.example.h_bank.data.dto.OperationRquestBody
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.domain.repository.IOperationRepository

class RepayLoanUseCase(
    private val operationRepository: IOperationRepository,
) {
    suspend operator fun invoke(
        senderAccountId: String,
        amount: Double,
    ): RequestResult<Unit> {
        val request = OperationRquestBody(
            senderAccountId = senderAccountId,
            recipientAccountId = senderAccountId,
            amount = amount,
            message = null,
            operationType = OperationType.LOAN_REPAYMENT
        )
        return operationRepository.createOperation(request)
    }
}