package com.example.mensageria.controllers;

import java.time.LocalDateTime;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mensageria.dto.DefaultMessageDto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/default")
public class DefaultController {

    @Autowired
    private AmqpTemplate defaultExchange;

    @PostMapping
    public DefaultMessageDto enviarParaFila(@RequestBody DefaultMessageDto defaultMessageDto) {
        defaultMessageDto.setLocalDateTime(LocalDateTime.now());

        defaultExchange.convertAndSend(defaultMessageDto);

        return defaultMessageDto;
    }
}
