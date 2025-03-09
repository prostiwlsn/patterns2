package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.payment.OperationTypeDto
import com.example.h_bank.data.dto.payment.OperationRquestBody
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
            operationType = OperationTypeDto.LOAN_REPAYMENT
        )
        return operationRepository.createOperation(request)
    }
}