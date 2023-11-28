package com.example.mensageria.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultExchangeConfig {

    private AmqpAdmin amqpAdmin;
    private final String DEFAULT_QUEUE;
}
