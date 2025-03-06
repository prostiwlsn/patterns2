package ru.hits.core.service.impl;

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

    @Autowired
    public RpcClientService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private Map<String, Object> sendRequest(
            String routingKey,
            String replyQueueName,
            byte[] requestBytes
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

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

    public UserDTO getUserInfo(
            UserInfoRequest requestMessage
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        byte[] requestBytes;

        try {
            requestBytes = objectMapper.writeValueAsBytes(requestMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing request");
        }

        var responseMap = sendRequest("userinfo", "userInfoResponseQueue", requestBytes);

        if (responseMap.containsKey("Success") && responseMap.get("Success").equals(false)) {
            throw new RuntimeException("Ошибка в получении информации о пользователе");
        }

        return parseUserDto((Map<String, Object>) responseMap.get("Data"));
    }

    public LoanPaymentSuccessResponse loanPaymentRequest(
            LoanPaymentRequest requestMessage
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        byte[] requestBytes;

        try {
            requestBytes = objectMapper.writeValueAsBytes(requestMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing request");
        }

        var responseMap = sendRequest("LoanPayment", "LoanPaymentResponse", requestBytes);

        if (responseMap.containsKey("Success") && responseMap.get("Success").equals(false)) {
            var response = parseLoanPaymentErrorResponse((Map<String, Object>) responseMap.get("Data"));
            throw new RuntimeException(response.getErrorMessage());
        }

        return parseLoanPaymentSuccessResponse((Map<String, Object>) responseMap.get("Data"));
    }

}