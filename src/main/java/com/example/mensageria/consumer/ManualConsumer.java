package com.example.mensageria.consumer;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.mensageria.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManualConsumer {

    private final AmqpAdmin amqpAdmin;
    private final RabbitTemplate rabbitTemplate;

    private int getCountOfMessages(String queueName) {

        Properties properties = amqpAdmin.getQueueProperties(queueName);
        return (int) properties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);
    }

    public List<MessageDto> receiveMessages(String queueName) {
        int count = getCountOfMessages(queueName);
        return IntStream.range(0, count)
                .mapToObj(
                        i -> (MessageDto) rabbitTemplate.receiveAndConvert(queueName))
                .collect(Collectors.toList());
    }
}
