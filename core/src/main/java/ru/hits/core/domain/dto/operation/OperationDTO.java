package ru.hits.core.domain.dto.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.core.domain.enums.OperationTypeEnum;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO, представляющий операцию со счётом")
public class OperationDTO {

    @Schema(description = "Уникальный идентификатор операции", example = "a1234567-b89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Идентификатор счёта отправителя", example = "c1234567-d89b-12d3-a456-426614174000")
    private UUID senderAccountId;

    @Schema(description = "Номер счёта отправителя", example = "1234 1234 1324 1324")
    private String senderAccountNumber;

    @Schema(description = "Идентификатор счёта получателя", example = "d1234567-e89b-12d3-a456-426614174000")
    private UUID recipientAccountId;

    @Schema(description = "Номер счёта отправителя", example = "1234 1234 1324 1324")
    private String recipientAccountNumber;

    @Schema(description = "Значение определяющее была ли операция плюсом к счету или минусом", example = "true")
    private Boolean directionToMe;

    @Schema(description = "Сумма операции", example = "500.75")
    private Float amount;

    @Schema(description = "Дата и время операции", example = "2024-02-22T14:30:00Z")
    private Instant transactionDateTime;

    @Schema(description = "Сообщение, сопровождающее операцию", example = "Оплата за услуги")
    private String message;

    @Schema(description = "Тип операции", example = "replenishment")
    private OperationTypeEnum operationType;

    @Schema(description = "Флаг просроченной оплаты кредита", example = "false")
    private Boolean isPaymentExpired;
}
