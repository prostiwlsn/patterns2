package ru.hits.core.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.core.domain.dto.account.AccountDTO;
import ru.hits.core.domain.entity.AccountEntity;
import ru.hits.core.domain.enums.OperationTypeEnum;
import ru.hits.core.service.FCMService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final FCMService fcmService;

    public void sendNotifications(
            OperationTypeEnum operationTypeEnum,
            AccountEntity sender,
            AccountEntity recipient,
            Float amount
    ) {
        if (operationTypeEnum.equals(OperationTypeEnum.TRANSFER)) {
            sendToClient(recipient.getUserId(), "Пополнение счёта", "Перевод от пользователя " + sender.getAccountNumber() + " на сумму " + amount);
            sendToEmployees("Перевод", "Перевод со счёта " + sender.getAccountNumber() + " на счёт " + recipient.getAccountNumber() + " на сумму " + amount);
        } else if (operationTypeEnum.equals(OperationTypeEnum.WITHDRAWAL)) {
            sendToClient(sender.getUserId(), "Снятие средств", "Снятие средств со счета " + sender.getAccountNumber() + " на сумму " + amount);
            sendToEmployees("Пользователь снял деньги", "Снятие средств со счета " + sender.getAccountNumber() + " на сумму " + amount);
        } else if (operationTypeEnum.equals(OperationTypeEnum.REPLENISHMENT)) {
            sendToClient(recipient.getUserId(), "Пополнение счёта", "Пополнение счёта " + recipient.getAccountNumber() + " на сумму " + amount);
            sendToEmployees("Пользователь пополнил счёт", "Пополнение счёта " + recipient.getAccountNumber() + " на сумму " + amount);
        } else if (operationTypeEnum.equals(OperationTypeEnum.LOAN_REPAYMENT)) {
            sendToClient(sender.getUserId(), "Оплата по кредиту", "Прошла оплата по кредиту со счёта " + sender.getAccountNumber() + " на сумму " + amount);
            sendToEmployees("Пользователь внес плату за кредит", "Прошла оплата по кредиту со счёта " + sender.getAccountNumber() + " на сумму " + amount);
        }
    }

    public void sendToClient(UUID userId, String title, String body) {
        var token = fcmService.getFCMToken(userId);

        if (token == null) {
            return;
        }

        sendNotification(token.getFcm(), title, body);
    }

    public void sendToEmployees(String title, String body) {
        List<String> employeeFcmTokens = fcmService.getEmployeeTokens();

        employeeFcmTokens.forEach(token -> sendNotification(token, title, body));
    }

    private void sendNotification(String token, String title, String body) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Не удалось отправить уведомление", e);
        }
    }
}