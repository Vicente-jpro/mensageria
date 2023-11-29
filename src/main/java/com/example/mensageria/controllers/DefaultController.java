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
@RequestMapping("/default")
@RequiredArgsConstructor
public class DefaultController {

    private final AmqpTemplate defaultExchange;

    @PostMapping
    public MessageDto enviarParaFila(@RequestBody MessageDto defaultMessageDto) {
        defaultMessageDto.setLocalDateTime(LocalDateTime.now());

        defaultExchange.convertAndSend(defaultMessageDto);

        return defaultMessageDto;
    }
}
