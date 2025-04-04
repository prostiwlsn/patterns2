package ru.hits.core.domain.dto.operation;

import lombok.Builder;
import lombok.Data;
import ru.hits.core.domain.enums.OperationTypeEnum;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class OperationFilters {

    private UUID userId;
    private UUID accountId;
    private Instant timeStart;
    private Instant timeEnd;
    private OperationTypeEnum operationType;
    private Boolean isExpired;
    private UUID loanAccountId;

}
