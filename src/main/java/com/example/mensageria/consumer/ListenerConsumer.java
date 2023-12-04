package com.example.mensageria.consumer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.example.mensageria.dto.MessageDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ListenerConsumer {

    private final String HEADER_X_RETRY = "x-try";
    private final int MAX_RETRY = 3;

    @Autowired
    private AmqpTemplate directExchange;

    @RabbitListener(queues = { "${rabbitmq.queues.direct-1}",
            "${rabbitmq.queues.direct-2}" }, containerFactory = "listenerContainerFactory")
    public void receiveMessages(@Payload MessageDto messageDto, Message message) {

        try {

            System.out.println(messageDto);
            // Erro provocado prepositadamente
            throw new RuntimeException();

        } catch (Exception e) {
            Integer retryCount = (Integer) message.getMessageProperties().getHeaders().get(HEADER_X_RETRY);
            if (retryCount == null)
                retryCount = 0;
            else if (retryCount >= MAX_RETRY) {
                log.info("Message was ignored.");
                return;
            }
            log.info("Retrying message for the {} time", retryCount);
            message.getMessageProperties().getHeaders().put(HEADER_X_RETRY, ++retryCount);
            directExchange.send(message.getMessageProperties().getReceivedRoutingKey(), message);
        }

    }
}
