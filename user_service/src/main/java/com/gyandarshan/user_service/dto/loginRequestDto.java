package com.gyandarshan.user_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class loginRequestDto {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter valid email")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    @Min(value = 8, message = "Password must be at least 8 characters long")
    private String password;
}
