package com.gyandarshan.user_service.dto;

import lombok.*;

@Data
@Builder
public class loginResponseDto {
    private String jwtToken;
//    private String email;
//    private boolean active;
//    private boolean emailVerified;
}
