package ru.hits.core.service.impl.rabbitmq.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hits.core.domain.dto.operation.LoanPaymentRequest;
import ru.hits.core.domain.dto.operation.LoanPaymentSuccessResponse;
import ru.hits.core.domain.dto.user.UserInfoRequest;
import ru.hits.core.domain.dto.user.UserDTO;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static ru.hits.core.utils.ParserUtils.*;

@Service
public class RpcClientService {

    private final RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public RpcClientService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    protected Map<String, Object> sendRequest(
            String routingKey,
            String replyQueueName,
            byte[] requestBytes
    ) throws JsonProcessingException {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

        String correlationId = UUID.randomUUID().toString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(replyQueueName);
        messageProperties.setCorrelationId(correlationId);

        Message message = new Message(requestBytes, messageProperties);

        rabbitTemplate.send("", routingKey, message);

        long startTime = System.currentTimeMillis();
        long timeout = 3000;

        while (System.currentTimeMillis() - startTime < timeout) {
            Message responseMessage = rabbitTemplate.receive(replyQueueName);

            if (responseMessage != null && correlationId.equals(responseMessage.getMessageProperties().getCorrelationId())) {
                String responseJson = new String(responseMessage.getBody(), StandardCharsets.UTF_8);
                return objectMapper.readValue(
                        responseJson, new TypeReference<Map<String, Object>>() {
                        }
                );
            }
        }

        throw new RuntimeException("Timeout or correlation ID mismatch");
    }

}