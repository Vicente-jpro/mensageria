package com.example.mensageria.controllers;

import java.time.LocalDateTime;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mensageria.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/fanout")
@RequiredArgsConstructor
public class FanoutController {

    private final AmqpTemplate fanoutExchange;

    @PostMapping
    public MessageDto sendMessageWithFanoutExchage(@RequestBody MessageDto messageDto) {
        messageDto.setLocalDateTime(LocalDateTime.now());

        fanoutExchange.convertAndSend(messageDto);
        return messageDto;
    }

}
