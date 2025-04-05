package ru.hits.core.service.impl.rabbitmq.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.hits.core.domain.dto.operation.CreateOperationDTO;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.dto.operation.OperationResponseWrapper;

@Component
@Slf4j
public class RabbitMQMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.rabbitmq.queues.operation}")
    private String operationQueue;

    @Value("${spring.rabbitmq.queues.operation-reply}")
    private String replyQueue;

    public RabbitMQMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @SneakyThrows
    public OperationDTO sendMessageAndWait(CreateOperationDTO createOperationDTO) {
        objectMapper.registerModule(new JavaTimeModule());

        String message = objectMapper.writeValueAsString(createOperationDTO);

        String response = (String) rabbitTemplate.convertSendAndReceive(operationQueue, message, msg -> {
            MessageProperties props = msg.getMessageProperties();
            props.setReplyTo(replyQueue);
            return msg;
        });

        OperationResponseWrapper wrapper = objectMapper.readValue(response, OperationResponseWrapper.class);

        if (!wrapper.isSuccess()) {
            throw new RuntimeException("Ошибка при создании операции: " + wrapper.getErrorMessage());
        }

        return wrapper.getData();
    }

}
