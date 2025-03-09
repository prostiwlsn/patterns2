package com.example.h_bank.domain.useCase

import com.example.h_bank.data.Operation
import com.example.h_bank.data.dto.payment.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.repository.IOperationRepository


class GetOperationInfoUseCase(
    private val operationRepository: IOperationRepository,
) {
    suspend operator fun invoke(operationId: String): RequestResult<Operation> {
        return operationRepository.getOperationInfo(operationId)
            .mapSuccess { operation -> operation.toDomain() }
    }
}