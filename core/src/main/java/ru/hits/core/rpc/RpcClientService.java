package ru.hits.core.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hits.core.domain.dto.user.RpcRequest;
import ru.hits.core.domain.dto.user.UserDTO;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static ru.hits.core.utils.ParserUtils.parseUserDto;

@Service
public class RpcClientService {

    private final RabbitTemplate rabbitTemplate;
    private final String replyQueueName = "responseQueue";

    @Autowired
    public RpcClientService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public UserDTO sendRequest(RpcRequest requestMessage) throws JsonProcessingException {
        String correlationId = UUID.randomUUID().toString();

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(replyQueueName);
        messageProperties.setCorrelationId(correlationId);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        byte[] requestBytes;

        try {
            requestBytes = objectMapper.writeValueAsBytes(requestMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing request");
        }

        Message message = new Message(requestBytes, messageProperties);

        rabbitTemplate.send("", "userinfo", message);

        long startTime = System.currentTimeMillis();
        long timeout = 3000;

        while (System.currentTimeMillis() - startTime < timeout) {
            Message responseMessage = rabbitTemplate.receive(replyQueueName);

            if (responseMessage != null && correlationId.equals(responseMessage.getMessageProperties().getCorrelationId())) {
                String responseJson = new String(responseMessage.getBody(), StandardCharsets.UTF_8);
                Map<String, Object> responseMap = objectMapper.readValue(
                        responseJson, new TypeReference<Map<String, Object>>() {}
                );

                if (responseMap.containsKey("Success") && responseMap.get("Success").equals(false)) {
                    throw new RuntimeException("Ошибка в получении информации о пользователе");
                }

                return parseUserDto((Map<String, Object>) responseMap.get("Data"));
            }
        }

        throw new RuntimeException("Timeout or correlation ID mismatch");
    }
}