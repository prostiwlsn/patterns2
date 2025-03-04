package ru.hits.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String REQUEST_QUEUE = "userinfo";
    public static final String REPLY_QUEUE = "responseQueue";

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE, true);
    }

    @Bean
    public Queue replyQueue() {
        return new Queue(REPLY_QUEUE, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("");
    }

    @Bean
    public Binding binding(Queue requestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(REQUEST_QUEUE);
    }
}