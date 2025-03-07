package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.repository.IOperationRepository
import com.example.h_bank.presentation.login.model.Operation


class GetOperationInfoUseCase(
    private val operationRepository: IOperationRepository,
) {
    suspend operator fun invoke(operationId: String): RequestResult<Operation> {
        return operationRepository.getOperationInfo(operationId)
            .mapSuccess { operation -> operation.toDomain() }
    }
}