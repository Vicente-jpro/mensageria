package com.example.mensageria.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
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
public class HeaderExchangeConfig {

    private final AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.header.exchange}")
    private String HEADER_EXCHANGE;

    @Value("${rabbitmq.queues.header-1}")
    private String HEADER_QUEUE_1;

    @Value("${rabbitmq.queues.header-2}")
    private String HEADER_QUEUE_2;

    public Queue createHeaderQueue1() {
        return new Queue(HEADER_QUEUE_1, true, false, false);
    }

    public Queue createHeaderQueue2() {
        return new Queue(HEADER_QUEUE_2, true, false, false);
    }

    public HeadersExchange createHeaderExchange() {
        return new HeadersExchange(HEADER_EXCHANGE, true, false);
    }

    // To accept it; error and debug (both of them ) should be contained
    public Binding createTopicBinding1() {
        return BindingBuilder.bind(createHeaderQueue1())
                .to(createHeaderExchange())
                .whereAll("error", "debug")
                .exist();
    }

    // To accept it; info or warning (one of them) will be enough.
    public Binding createTopicBinding2() {
        return BindingBuilder.bind(createHeaderQueue2())
                .to(createHeaderExchange())
                .whereAny("info", "warning")
                .exist();
    }

    @Bean
    public AmqpTemplate headerExchange(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(HEADER_EXCHANGE);

        return rabbitTemplate;
    }

    @PostConstruct
    public void init() {

        amqpAdmin.declareQueue(createHeaderQueue1());
        amqpAdmin.declareQueue(createHeaderQueue2());
        amqpAdmin.declareExchange(createHeaderExchange());
        amqpAdmin.declareBinding(createTopicBinding1());
        amqpAdmin.declareBinding(createTopicBinding2());

    }

}
