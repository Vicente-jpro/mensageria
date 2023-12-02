package com.example.mensageria.controllers;

import java.time.LocalDateTime;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mensageria.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/header")
@RequiredArgsConstructor
public class HeaderController {

    private final AmqpTemplate headerExchange;

    @GetMapping
    public MessageDto sendMessageWithFanoutExchage(
            @RequestParam(value = "query", required = false) String header,
            @RequestBody MessageDto messageDto) {
        messageDto.setLocalDateTime(LocalDateTime.now());

        MessageBuilder messageBuilder = MessageBuilder.withBody(messageDto.toString().getBytes());

        switch (header) {
            case "error":
                messageBuilder.setHeader("error", header);
                break;

            case "debug":
                messageBuilder.setHeader("debug", header);
                break;

            case "info":
                messageBuilder.setHeader("info", header);
                break;

            case "warning":
                messageBuilder.setHeader("warning", header);
                break;
            default:
                break;
        }

        headerExchange.convertAndSend(messageBuilder.build());
        return messageDto;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello word";
    }
}
