package ru.hits.core.service.impl.rabbitmq.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class RpcClientService {

    private final RabbitTemplate rabbitTemplate;

    private final RabbitAdmin rabbitAdmin;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public RpcClientService(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    protected Map<String, Object> sendRequest(
            String routingKey,
            byte[] requestBytes
    ) throws JsonProcessingException {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

        String correlationId = UUID.randomUUID().toString();
        String replyQueueName = "tmp.reply." + correlationId;

        Queue replyQueue = new Queue(replyQueueName, false, true, true);
        rabbitAdmin.declareQueue(replyQueue);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(replyQueueName);
        messageProperties.setCorrelationId(correlationId);

        Message message = new Message(requestBytes, messageProperties);

        rabbitTemplate.send("", routingKey, message);

        long startTime = System.currentTimeMillis();
        long timeout = 10000;

        while (System.currentTimeMillis() - startTime < timeout) {
            Message responseMessage = rabbitTemplate.receive(replyQueueName);

            if (responseMessage != null && correlationId.equals(responseMessage.getMessageProperties().getCorrelationId())) {
                String responseJson = new String(responseMessage.getBody(), StandardCharsets.UTF_8);

                rabbitAdmin.deleteQueue(replyQueueName);

                return objectMapper.readValue(
                        responseJson, new TypeReference<Map<String, Object>>() {
                        }
                );
            }
        }

        rabbitAdmin.deleteQueue(replyQueueName);

        throw new RuntimeException("Timeout or correlation ID mismatch");
    }

}