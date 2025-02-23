package ru.hits.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.dto.operation.OperationShortDTO;

import java.util.UUID;

public interface OperationService {

    OperationDTO createOperation(OperationRequestBody operationRequestBody);

    Page<OperationShortDTO> getOperations(UUID accountId, Pageable pageable);

    OperationDTO getOperation(UUID operationId);

}
