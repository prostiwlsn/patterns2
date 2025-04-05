package ru.hits.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import ru.hits.core.service.impl.RabbitMQListener;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queues.user-info}")
    private String REQUEST_QUEUE;
    @Value("${spring.rabbitmq.queues.loan}")
    private String LOAN_REQUEST_QUEUE;
    @Value("${spring.rabbitmq.queues.operation}")
    private String OPERATION_REQUEST_QUEUE;
    @Value("${spring.rabbitmq.queues.user-info-reply}")
    private String REPLY_QUEUE;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE, true);
    }

    @Bean
    public Queue loanRequestQueue() {
        return new Queue(LOAN_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue operationRequestQueue() {
        return new Queue(OPERATION_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue replyQueue() {
        return new Queue(REPLY_QUEUE, true);
    }

    @Bean
    public Queue masterAccountQueue() {
        return new Queue("masterAccount", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("");
    }

    @Bean
    public DirectExchange loanExchange() {
        return new DirectExchange("GetLoan");
    }

    @Bean
    public Binding binding(Queue requestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(REQUEST_QUEUE);
    }

    @Bean
    public Binding loanRequestBinding(Queue loanRequestQueue, DirectExchange loanExchange) {
        return BindingBuilder.bind(loanRequestQueue).to(loanExchange).with(LOAN_REQUEST_QUEUE);
    }

    @Bean
    public Binding operationRequestBinding(Queue operationRequestQueue, DirectExchange loanExchange) {
        return BindingBuilder.bind(operationRequestQueue).to(loanExchange).with(OPERATION_REQUEST_QUEUE);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RabbitMQListener listener) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener, "receiveMessage");
        adapter.setMessageConverter(converter);
        return adapter;
    }

    @Bean
    public SimpleMessageListenerContainer loanRequestContainer(ConnectionFactory connectionFactory,
                                                               MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(LOAN_REQUEST_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }
}