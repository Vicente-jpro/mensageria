package com.example.mensageria.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DefaultExchangeConfig {

    private final AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.queues.default}")
    private String DEFAULT_QUEUE;

    public Queue createQueue() {
        return new Queue(DEFAULT_QUEUE, true, false, false);
    }

    @Bean
    public AmqpTemplate defaultExchange(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setRoutingKey(DEFAULT_QUEUE);

        return rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(createQueue());
    }
}
