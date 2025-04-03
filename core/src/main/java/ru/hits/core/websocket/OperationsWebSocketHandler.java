package ru.hits.core.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.hits.core.domain.dto.user.UserInfoRequest;
import ru.hits.core.service.impl.JwtService;
import ru.hits.core.service.impl.rabbitmq.rpc.UserInfoService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class OperationsWebSocketHandler extends TextWebSocketHandler {

    private final UserInfoService userInfoService;

    private final JwtService jwtService;

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String token = (String) attributes.get("token");

        userInfoService.getUserInfo(new UserInfoRequest(
                jwtService.getUserId("Bearer " + token),
                token
        ));

        String accountId = session.getUri().getQuery().split("=")[1];
        sessions.put(accountId, session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.values().remove(session);
    }

    public void sendUpdate(String accountId, String message) {
        WebSocketSession session = sessions.get(accountId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}