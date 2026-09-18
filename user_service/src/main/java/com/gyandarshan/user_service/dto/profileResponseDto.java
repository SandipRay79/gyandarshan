package com.gyandarshan.user_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class profileResponseDto {
    private int id;
    private String name;
    private String email;
    private boolean active;
    private boolean emailVerified;
    private String createdAt;
}
