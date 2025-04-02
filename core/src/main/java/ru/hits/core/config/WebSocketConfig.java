package ru.hits.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.hits.core.websocket.OperationsWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final OperationsWebSocketHandler operationsWebSocketHandler;

    public WebSocketConfig(OperationsWebSocketHandler operationsWebSocketHandler) {
        this.operationsWebSocketHandler = operationsWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(operationsWebSocketHandler, "/ws/operations")
                .addInterceptors(new AuthInterceptor())
                .setAllowedOrigins("*");
    }
}