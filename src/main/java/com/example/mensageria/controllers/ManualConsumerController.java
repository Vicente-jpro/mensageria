package com.example.mensageria.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mensageria.consumer.ManualConsumer;
import com.example.mensageria.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/manual-consumer")
@RequiredArgsConstructor
public class ManualConsumerController {

    private final ManualConsumer manualConsumer;

    @GetMapping("/{queueName}")
    public List<MessageDto> consumeAllMessagesOfQueue(@PathVariable("queueName") String queueName) {
        return manualConsumer.receiveMessages(queueName);
    }

}
