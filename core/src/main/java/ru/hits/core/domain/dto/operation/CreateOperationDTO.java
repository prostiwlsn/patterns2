package ru.hits.core.domain.dto.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.core.domain.enums.OperationTypeEnum;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOperationDTO {

    @Schema(description = "Идентификатор счёта отправителя (может быть null, если это пополнение)",
            example = "c1234567-d89b-12d3-a456-426614174000")
    @Nullable
    private UUID senderAccountId;

    @Schema(description = "Идентификатор счёта получателя", example = "d1234567-e89b-12d3-a456-426614174000")
    @Nullable
    private UUID recipientAccountId;

    @Schema(description = "Сумма операции", example = "500.75")
    private Float amount;

    @Schema(description = "Сообщение, сопровождающее операцию", example = "Оплата за услуги")
    @Nullable
    private String message;

    @Schema(description = "Тип операции", example = "WITHDRAWAL")
    private OperationTypeEnum operationType;

    private UUID userId;

}
