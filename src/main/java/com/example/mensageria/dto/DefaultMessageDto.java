package com.example.mensageria.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefaultMessageDto {

    private String type;
    private LocalDateTime localDateTime;
}
