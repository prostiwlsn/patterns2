package ru.hits.core.service.impl.rabbitmq.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.domain.dto.account.MasterAccountResponseWrapper;
import ru.hits.core.domain.dto.operation.CreateOperationDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.dto.operation.OperationResponseWrapper;
import ru.hits.core.exceptions.ForbiddenException;
import ru.hits.core.service.AccountService;
import ru.hits.core.service.OperationService;
import ru.hits.core.service.impl.JwtService;

@Component
@Slf4j
@RequiredArgsConstructor
public class MasterAccountHandler {

    private final AccountService accountService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @RabbitListener(queues = "masterAccount")
    public String receiveMessage(String message) {
        log.info("Получено сообщение: " + message);
        objectMapper.registerModule(new JavaTimeModule());

        try {
            var response = accountService.getMasterAccount();

            MasterAccountResponseWrapper wrapper =
                    new MasterAccountResponseWrapper(true, response, null);
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
