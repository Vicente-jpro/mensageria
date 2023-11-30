package com.example.mensageria.controllers;

import java.time.LocalDateTime;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mensageria.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {

    private AmqpTemplate topicExchange;

    @PostMapping("/{key}")
    public MessageDto sendMessageWithTopicExchage(
            @RequestBody MessageDto messageDto, @PathVariable("key") String key) {
        messageDto.setLocalDateTime(LocalDateTime.now());
        topicExchange.convertAndSend(key, messageDto);
        return messageDto;
    }
}
