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
public class OperationDTO {

    @Schema(description = "Уникальный идентификатор операции", example = "a1234567-b89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Идентификатор счёта отправителя (может быть null, если это пополнение)",
            example = "c1234567-d89b-12d3-a456-426614174000")
    private UUID senderAccountId;

    @Schema(description = "Идентификатор счёта получателя", example = "d1234567-e89b-12d3-a456-426614174000")
    private UUID recipientAccountId;

    @Schema(description = "Сумма операции", example = "500.75")
    private Float amount;

    @Schema(description = "Дата и время операции", example = "2024-02-22T14:30:00Z")
    private Instant transactionDateTime;

    @Schema(description = "Сообщение, сопровождающее операцию", example = "Оплата за услуги")
    private String message;

    @Schema(description = "Тип операции", example = "WITHDRAWAL")
    private OperationTypeEnum operationType;
}
