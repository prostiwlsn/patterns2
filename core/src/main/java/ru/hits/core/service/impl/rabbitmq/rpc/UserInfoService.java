package ru.hits.core.service.impl.rabbitmq.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.hits.core.domain.dto.user.UserDTO;
import ru.hits.core.domain.dto.user.UserInfoRequest;

import java.util.Map;

import static ru.hits.core.utils.ParserUtils.parseUserDto;

@Service
@Slf4j
public class UserInfoService extends RpcClientService {

    private ObjectMapper objectMapper = new ObjectMapper();

    public UserInfoService(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        super(rabbitTemplate, rabbitAdmin);
    }

    public UserDTO getUserInfo(
            UserInfoRequest requestMessage
    ) throws JsonProcessingException {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
        byte[] requestBytes;

        try {
            requestBytes = objectMapper.writeValueAsBytes(requestMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing request");
        }

        var responseMap = sendRequest("userinfo", requestBytes);

        if (responseMap.containsKey("Success") && responseMap.get("Success").equals(false)) {
            log.error("Неудачное сообщение: " + responseMap);
            throw new RuntimeException("Ошибка в получении информации о пользователе");
        }

        return parseUserDto((Map<String, Object>) responseMap.get("Data"));
    }

}
