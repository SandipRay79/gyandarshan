package com.gyandarshan.user_service.controller;

import com.gyandarshan.user_service.Filter.JwtUtil;
import com.gyandarshan.user_service.dto.loginRequestDto;
import com.gyandarshan.user_service.dto.loginResponseDto;
import com.gyandarshan.user_service.dto.registerDto;
import com.gyandarshan.user_service.repository.UserRepository;
import com.gyandarshan.user_service.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/gyandarshan")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private AuthenticationManager authmngr;

    @Autowired
    private JwtUtil jwtUtil;

    private final UserService UserService;
    private final UserRepository UserRepository;

    public UserController(UserService UserService, UserRepository UserRepository) {
        this.UserService = UserService;
        this.UserRepository = UserRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody registerDto registerDto) {
        // Perform registration logic here
//        JsonMapper jsonMapper = JsonMapper.builder().build();
//        String jsonString = jsonMapper.writeValueAsString(registerDto);
//        System.out.println("Received JSON: " + jsonString);
//        return ResponseEntity.ok(jsonString);

        try {
            String savedUser = UserService.registerUser(registerDto);
            HttpStatus status = HttpStatus.CREATED;

            Map<String, Object> response = new HashMap<>();
            response.put("status", status.value()); // 201
            response.put("message", "User created successfully");
            response.put("data", savedUser);

            return new ResponseEntity<>(response, status);
        } catch(DataIntegrityViolationException e) {
            // Handle the case where the email already exists
            HttpStatus status = HttpStatus.CONFLICT; // 409 Conflict

            Map<String, Object> response = new HashMap<>();
            response.put("status", status.value());
            response.put("message", "An account with the email already exists");
            response.put("data", null);

            return new ResponseEntity<>(response, status);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequestDto loginDto) {
        try {
        // Perform login logic here
            Authentication authentication = authmngr.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            UserDetails userDetails = (UserDetails)authentication.getPrincipal();
            String token = this.jwtUtil.generateToken(userDetails.getUsername());
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
    //                .secure(true)
                    .secure(false) // Set to true in production for HTTPS
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Lax")
                    .build();
            loginResponseDto response = loginResponseDto.builder()
                    .jwtToken(token)
                    .build();
            //return new ResponseEntity<>(response, HttpStatus.OK);
            return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(response);
        } catch (Exception e) {
            // Handle authentication failure
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "Invalid email or password");
            response.put("data", null);

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true).secure(true).sameSite("Strict").path("/").maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }
}

