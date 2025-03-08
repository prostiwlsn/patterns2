package ru.hits.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import ru.hits.core.domain.dto.loan.LoanRequest;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.enums.OperationTypeEnum;
import ru.hits.core.service.OperationService;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQListener {

    private final OperationService operationService;
    private final ObjectMapper objectMapper;

    public void receiveMessage(Message message) throws JsonProcessingException {
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);

        System.out.println("Получено сообщение из RabbitMQ:");
        System.out.println("Body: " + messageBody);

        LoanRequest loanRequest = null;
        try {
            loanRequest = objectMapper.readValue(messageBody, LoanRequest.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

        operationService.createOperation(
                null,
                new OperationRequestBody(
                        loanRequest.getAccountId(),
                        null,
                        loanRequest.getAmount(),
                        null,
                        OperationTypeEnum.REPLENISHMENT
                )
        );

    }
}