package com.example.mensageria.controllers;

import java.time.LocalDateTime;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mensageria.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/direct")
@RequiredArgsConstructor
public class DirectController {

    private final AmqpTemplate directExchange;

    @Value("${rabbitmq.direct.routing-key-1}")
    private String ROUTING_KEY_1;

    @Value("${rabbitmq.direct.routing-key-2}")
    private String ROUTING_KEY_2;

    @PostMapping("/{key}")
    public MessageDto sendMessageWithDirectExgeng(
            @RequestBody MessageDto messageDto, @PathVariable("key") Integer key) {

        messageDto.setType(messageDto.getType() + key);
        messageDto.setLocalDateTime(LocalDateTime.now());

        String routingKey = 1 == key ? ROUTING_KEY_1 : ROUTING_KEY_2;
        directExchange.convertAndSend(routingKey, messageDto);

        return messageDto;
    }

}
