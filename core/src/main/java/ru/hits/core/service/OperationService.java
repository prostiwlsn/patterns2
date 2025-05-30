package ru.hits.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.dto.operation.OperationShortDTO;
import ru.hits.core.domain.enums.OperationTypeEnum;

import java.time.Instant;
import java.util.UUID;

public interface OperationService {

    OperationDTO createOperation(UUID userId, OperationRequestBody operationRequestBody) throws JsonProcessingException;

    Page<OperationShortDTO> getOperations(
            UUID userId,
            UUID accountId,
            Instant timeStart,
            Instant timeEnd,
            OperationTypeEnum operationType,
            Pageable pageable
    ) throws JsonProcessingException;

    OperationDTO getOperation(UUID userId, UUID accountId, UUID operationId) throws JsonProcessingException;

}
