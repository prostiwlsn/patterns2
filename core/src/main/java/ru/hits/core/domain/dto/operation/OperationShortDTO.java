package ru.hits.core.domain.dto.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.hits.core.domain.enums.OperationTypeEnum;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Schema(description = "DTO, представляющий операцию со счётом")
public class OperationShortDTO {

    @Schema(description = "Уникальный идентификатор операции", example = "a1234567-b89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Сумма операции", example = "500.75")
    private Float amount;

    @Schema(description = "Значение определяющее была ли операция плюсом к счету или минусом", example = "true")
    private Boolean directionToMe;

    @Schema(description = "Дата и время операции", example = "2024-02-22T14:30:00Z")
    private Instant transactionDateTime;

    @Schema(description = "Тип операции", example = "WITHDRAWAL")
    private OperationTypeEnum operationType;
}
