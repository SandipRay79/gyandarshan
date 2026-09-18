package com.gyandarshan.user_service.dto;

import com.gyandarshan.user_service.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class registerDto {
        private String name;
        private String email;
        private String password;
}
