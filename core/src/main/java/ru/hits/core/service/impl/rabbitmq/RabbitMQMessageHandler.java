package ru.hits.core.service.impl.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.hits.core.domain.dto.operation.CreateOperationDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.service.OperationService;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQMessageHandler {

    private final OperationService operationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @RabbitListener(queues = "operation")
    public String receiveMessage(String message) {
        log.info("Получено сообщение: " + message);
        objectMapper.registerModule(new JavaTimeModule());

        CreateOperationDTO createOperationDTO = objectMapper.readValue(message, CreateOperationDTO.class);

        var response = operationService.createOperation(
                createOperationDTO.getUserId(), new OperationRequestBody(
                        createOperationDTO.getSenderAccountId(),
                        createOperationDTO.getRecipientAccountId(),
                        createOperationDTO.getAmount(),
                        createOperationDTO.getMessage(),
                        createOperationDTO.getOperationType()
                )
        );

        return objectMapper.writeValueAsString(response);
    }

}
