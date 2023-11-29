package com.example.mensageria.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDto {

    private String type;
    private LocalDateTime localDateTime;
}
