package com.gyandarshan.user_service.service;

import com.gyandarshan.user_service.dto.registerDto;
import com.gyandarshan.user_service.entity.Role;
import com.gyandarshan.user_service.entity.User;
import com.gyandarshan.user_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
//                .roles(user.getRole())
                .build();
    }

    public String registerUser(registerDto dto) {
        // Perform registration logic here
        // For example, you can save the user to the database using the userRepository
        // You can also add validation and error handling as needed

        // Example code to save the user (assuming you have a User entity)
         User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        newUser.setRole(Role.USER);
        newUser.setActive(true);
        newUser.setEmailVerified(false);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(newUser);

        return "User registered successfully";
    }
}
