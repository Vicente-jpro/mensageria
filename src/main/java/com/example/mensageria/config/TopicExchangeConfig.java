package com.example.mensageria.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
public class TopicExchangeConfig {

    private final AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.topic.exchange}")
    private String TOPIC_EXCHANGE;

    @Value("${rabbitmq.queues.topic-1}")
    private String TOPIC_QUEUE_1;

    @Value("${rabbitmq.queues.topic-2}")
    private String TOPIC_QUEUE_2;

    @Value("${rabbitmq.queues.topic-3}")
    private String TOPIC_QUEUE_3;

    @Value("${rabbitmq.topic.pattern-1}")
    private String TOPIC_PATTERN_1;

    @Value("${rabbitmq.topic.pattern-2}")
    private String TOPIC_PATTERN_2;

    @Value("${rabbitmq.topic.pattern-3}")
    private String TOPIC_PATTERN_3;

    public Queue createTopicQueue1() {
        return new Queue(TOPIC_QUEUE_1, true, false, false);
    }

    public Queue createTopicQueue2() {
        return new Queue(TOPIC_QUEUE_2, true, false, false);
    }

    public Queue createTopicQueue3() {
        return new Queue(TOPIC_QUEUE_3, true, false, false);
    }

    public TopicExchange createTopicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    public Binding createTopicBinding1() {
        return BindingBuilder.bind(createTopicQueue1())
                .to(createTopicExchange())
                .with(TOPIC_PATTERN_1);
    }

    public Binding createTopicBinding2() {
        return BindingBuilder.bind(createTopicQueue2())
                .to(createTopicExchange())
                .with(TOPIC_PATTERN_2);
    }

    public Binding createTopicBinding3() {
        return BindingBuilder.bind(createTopicQueue3())
                .to(createTopicExchange())
                .with(TOPIC_PATTERN_3);
    }

    @Bean
    public AmqpTemplate topicExchange(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(TOPIC_EXCHANGE);

        return rabbitTemplate;
    }

    @PostConstruct
    public void init() {

        amqpAdmin.declareQueue(createTopicQueue1());
        amqpAdmin.declareQueue(createTopicQueue2());
        amqpAdmin.declareQueue(createTopicQueue3());
        amqpAdmin.declareExchange(createTopicExchange());
        amqpAdmin.declareBinding(createTopicBinding1());
        amqpAdmin.declareBinding(createTopicBinding2());
        amqpAdmin.declareBinding(createTopicBinding3());

    }

}
