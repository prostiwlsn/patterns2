package ru.hits.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.hits.core.domain.dto.loan.LoanRequest;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.enums.OperationTypeEnum;
import ru.hits.core.service.OperationService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQListener {

    private final OperationService operationService;

    public void receiveMessage(LinkedHashMap<String, Object> messageMap) {
        System.out.println("Получено сообщение из RabbitMQ:");
        System.out.println("Body: " + messageMap);

        try {
            var request = LoanRequest.builder()
                    .accountId(getUUID(messageMap, "AccountId"))
                    .amount(getFloat(messageMap, "Amount"))
                    .build();

            operationService.createOperation(
                    null,
                    new OperationRequestBody(
                            null,
                            request.getAccountId(),
                            request.getAmount(),
                            null,
                            OperationTypeEnum.REPLENISHMENT
                    )
            );

            operationService.masterAccountWithdrawal(
                    request.getAmount()
            );
        } catch (Exception e) {
            log.error("Ошибка обработки сообщения: " + e.getMessage());
        }
    }

    public static UUID getUUID(Map<String, Object> map, String key) {
        return Optional.ofNullable(map.get(key))
                .map(Object::toString)
                .map(UUID::fromString)
                .orElseThrow(() -> new IllegalArgumentException("Invalid UUID for key: " + key));
    }

    public static float getFloat(Map<String, Object> map, String key) {
        return Optional.ofNullable(map.get(key))
                .map(Object::toString)
                .map(Float::parseFloat)
                .orElseThrow(() -> new IllegalArgumentException("Invalid float for key: " + key));
    }
}