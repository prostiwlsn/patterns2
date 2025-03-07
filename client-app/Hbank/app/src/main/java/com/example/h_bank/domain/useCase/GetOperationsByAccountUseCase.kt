package com.example.h_bank.domain.useCase

import com.example.h_bank.data.dto.OperationShortDto
import com.example.h_bank.data.dto.PageResponse
import com.example.h_bank.data.dto.Pageable
import com.example.h_bank.data.dto.toDomain
import com.example.h_bank.data.utils.RequestResult
import com.example.h_bank.data.utils.mapSuccess
import com.example.h_bank.domain.model.OperationShort
import com.example.h_bank.domain.repository.IOperationRepository

class GetOperationsByAccountUseCase(
    private val operationRepository: IOperationRepository
) {
    suspend operator fun invoke(
        accountId: String,
        pageable: Pageable = Pageable()
    ): RequestResult<PageResponse<OperationShort>> {
        return operationRepository.getOperationsByAccount(accountId, pageable)
            .mapSuccess { pageResponse: PageResponse<OperationShortDto> ->
                PageResponse(
                    content = pageResponse.content.map { it.toDomain() },
                    totalPages = pageResponse.totalPages,
                    totalElements = pageResponse.totalElements,
                    first = pageResponse.first,
                    last = pageResponse.last,
                    size = pageResponse.size,
                    number = pageResponse.number,
                    sort = pageResponse.sort,
                    pageable = pageResponse.pageable,
                    numberOfElements = pageResponse.numberOfElements,
                    empty = pageResponse.empty
                )
            }
    }
}
