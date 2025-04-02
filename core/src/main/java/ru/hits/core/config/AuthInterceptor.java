package ru.hits.core.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class AuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            if (request instanceof org.springframework.http.server.ServletServerHttpRequest) {
                HttpHeaders headers = (request).getHeaders();
                String authHeader = headers.getFirst("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    attributes.put("token", authHeader.substring(7));
                } else {
                    return false;
                }
            }
            return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
