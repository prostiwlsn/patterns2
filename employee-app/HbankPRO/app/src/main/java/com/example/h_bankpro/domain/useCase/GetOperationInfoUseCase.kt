package com.example.h_bankpro.domain.useCase

import com.example.h_bankpro.data.dto.toDomain
import com.example.h_bankpro.data.utils.RequestResult
import com.example.h_bankpro.data.utils.mapSuccess
import com.example.h_bankpro.domain.model.Operation
import com.example.h_bankpro.domain.repository.IOperationRepository

class GetOperationInfoUseCase(
    private val operationRepository: IOperationRepository,
) {
    suspend operator fun invoke(accountId: String, operationId: String): RequestResult<Operation> {
        return operationRepository.getOperationInfo(accountId, operationId)
            .mapSuccess { operation -> operation.toDomain() }
    }
}