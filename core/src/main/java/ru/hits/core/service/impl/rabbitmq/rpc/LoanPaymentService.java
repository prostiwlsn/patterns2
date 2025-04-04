package ru.hits.core.service.impl.rabbitmq.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.hits.core.domain.dto.operation.LoanPaymentRequest;
import ru.hits.core.domain.dto.operation.LoanPaymentSuccessResponse;

import java.util.Map;

import static ru.hits.core.utils.ParserUtils.parseLoanPaymentSuccessResponse;

@Service
@Slf4j
public class LoanPaymentService extends RpcClientService {

    private ObjectMapper objectMapper = new ObjectMapper();

    public LoanPaymentService(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        super(rabbitTemplate, rabbitAdmin);
    }

    public LoanPaymentSuccessResponse loanPaymentRequest(
            LoanPaymentRequest requestMessage
    ) throws JsonProcessingException {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
        byte[] requestBytes;

        try {
            requestBytes = objectMapper.writeValueAsBytes(requestMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing request");
        }

        var responseMap = sendRequest("LoanPayment", requestBytes);

        if (responseMap.containsKey("Success") && responseMap.get("Success").equals(false)) {
            log.error("Неудачное сообщение: " + responseMap);
            var response = (String) responseMap.get("ErrorMessage");
            throw new RuntimeException(response.toString());
        }

        return parseLoanPaymentSuccessResponse((Map<String, Object>) responseMap.get("Data"));
    }

}
