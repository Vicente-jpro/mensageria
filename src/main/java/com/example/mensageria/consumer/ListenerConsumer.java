package com.example.mensageria.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.mensageria.dto.MessageDto;

@Component
public class ListenerConsumer {

    @RabbitListener(queues = { "${rabbitmq.queues.direct-1}",
            "${rabbitmq.queues.direct-2}" }, containerFactory = "listenerContainerFactory")
    public void receiveMessages(MessageDto messageDto) {
        System.out.println(messageDto);
    }
}
