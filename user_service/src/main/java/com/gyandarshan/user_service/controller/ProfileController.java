package com.gyandarshan.user_service.controller;

import com.gyandarshan.user_service.Filter.JwtUtil;
import com.gyandarshan.user_service.dto.profileResponseDto;
import com.gyandarshan.user_service.entity.User;
import com.gyandarshan.user_service.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/gyandarshan")
public class ProfileController {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public ProfileController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

//    @GetMapping("/profile")
//    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or malformed Authorization header");
//        }
//
//        String token = authHeader.substring(7); // safer than .replace() — won't touch "Bearer" if it appears elsewhere
//
//        if (!jwtUtil.validateToken(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//        }
//
//        String email = jwtUtil.extractUsername(token);
//
//        User user = userRepository.findByEmail(email).orElse(null);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        Map<String, Object> profile = new HashMap<>();
//        profile.put("id", user.getId());
//        profile.put("name", user.getName());
//        profile.put("email", user.getEmail());
//        profile.put("role", user.getRole());
//        profile.put("active", user.isActive());
//        profile.put("emailVerified", user.isEmailVerified());
//
//        return ResponseEntity.ok(profile);
//        //return ResponseEntity.ok("Profile retrieved successfully");
//    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        String email = authentication.getName(); // comes from JwtFilter

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("createdAt from DB = " + user.getCreatedAt());

        // Use a DTO instead of manual mapping
        profileResponseDto response = profileResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .active(user.isActive())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt() != null
                        ? user.getCreatedAt().toLocalDate().format(formatter)
                        : null)
                .build();

        return ResponseEntity.ok(response);
    }

}
