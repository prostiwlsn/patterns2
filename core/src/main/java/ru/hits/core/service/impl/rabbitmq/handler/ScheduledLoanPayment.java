package ru.hits.core.service.impl.rabbitmq.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.hits.core.domain.dto.account.MasterAccountResponseWrapper;
import ru.hits.core.domain.dto.operation.CreateOperationDTO;
import ru.hits.core.domain.dto.operation.OperationRequestBody;
import ru.hits.core.domain.dto.operation.OperationResponseWrapper;
import ru.hits.core.domain.dto.operation.ScheduledPayDTO;
import ru.hits.core.domain.enums.OperationTypeEnum;
import ru.hits.core.service.AccountService;
import ru.hits.core.service.OperationService;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledLoanPayment {

    private final OperationService operationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @RabbitListener(queues = "paymentJob")
    public void receiveMessage(String message) {
        log.info("Получено сообщение: " + message);
        objectMapper.registerModule(new JavaTimeModule());

        try {
            ScheduledPayDTO scheduledPayDTO = objectMapper.readValue(message, ScheduledPayDTO.class);

            operationService.createOperation(
                    null,
                    new OperationRequestBody(
                            scheduledPayDTO.getLoanId(),
                            scheduledPayDTO.getAccountId(),
                            scheduledPayDTO.getAmount(),
                            null,
                            OperationTypeEnum.LOAN_REPAYMENT
                    )
            );

        } catch (Exception e) {
            log.error("Ошибка при обработке операции: {}", e.getMessage());
        }
    }

}
