package ru.hits.core.service.impl.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.hits.core.domain.dto.operation.CreateOperationDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.dto.operation.OperationResponseWrapper;
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

        try {
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

            OperationResponseWrapper wrapper = new OperationResponseWrapper(true, response, null);
            return objectMapper.writeValueAsString(wrapper);

        } catch (Exception e) {
            log.error("Ошибка при обработке операции: {}", e.getMessage());
            OperationResponseWrapper errorWrapper = new OperationResponseWrapper(false, null, e.getMessage());
            try {
                return objectMapper.writeValueAsString(errorWrapper);
            } catch (JsonProcessingException jsonEx) {
                log.error("Ошибка сериализации ошибки: {}", jsonEx.getMessage());
                return "{\"success\":false,\"errorMessage\":\"Internal error\"}";
            }
        }
    }

}
