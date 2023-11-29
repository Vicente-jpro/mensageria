package com.example.mensageria.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
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
public class FanoutExchangeConfig {

    private final AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.fanout.exchange}")
    private String FANOUT_EXCHANGE;

    @Value("${rabbitmq.queues.fanout-1}")
    private String FANOUT_QUEUE_1;

    @Value("${rabbitmq.queues.fanout-2}")
    private String FANOUT_QUEUE_2;

    public Queue createFanoutQueue1() {
        return new Queue(FANOUT_QUEUE_1, true, false, false);
    }

    public Queue createFanoutQueue2() {
        return new Queue(FANOUT_QUEUE_2, true, false, false);
    }

    public FanoutExchange createFanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

    public Binding createFanoutBinding1() {
        return BindingBuilder.bind(createFanoutQueue1())
                .to(createFanoutExchange());
    }

    public Binding createFanoutBinding2() {
        return BindingBuilder.bind(createFanoutQueue2())
                .to(createFanoutExchange());
    }

    @Bean
    public AmqpTemplate fanoutExchange(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(FANOUT_EXCHANGE);

        return rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        amqpAdmin.declareQueue(createFanoutQueue1());
        amqpAdmin.declareQueue(createFanoutQueue2());
        amqpAdmin.declareExchange(createFanoutExchange());
        amqpAdmin.declareBinding(createFanoutBinding1());
        amqpAdmin.declareBinding(createFanoutBinding2());
    }

}
