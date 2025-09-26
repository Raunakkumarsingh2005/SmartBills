package com.spring.smartbills.service.impl;

import com.spring.smartbills.dtos.LoginDto;
import com.spring.smartbills.dtos.ResponseDto;
import com.spring.smartbills.dtos.SignUpDto;
import com.spring.smartbills.entity.PasswordResetToken;
import com.spring.smartbills.entity.User;
import com.spring.smartbills.repository.PasswordResetTokenRepository;
import com.spring.smartbills.repository.UserRepository;
import com.spring.smartbills.security.jwt.JwtUtils;
import com.spring.smartbills.service.UserService;
import com.spring.smartbills.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        try {
            Optional<User> userOptional = userRepository.findByUserName(loginDto.getUserName());
            
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
                if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Login successful");
                    response.put("user", user);
                    response.put("Jwt", jwtUtils.generateJwtToken(userDetails));
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ResponseDto("401", "Invalid credentials"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseDto("404", "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Login failed: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> register(SignUpDto signUpDto) {
        try {
            if (userRepository.existsByUserName(signUpDto.getUserName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResponseDto("409", "Username is already taken"));
            }

            if (userRepository.existsByEmail(signUpDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ResponseDto("409", "Email is already in use"));
            }

            User user = new User();
            user.setUserName(signUpDto.getUserName());
            user.setEmail(signUpDto.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            user.setEnabled(true);

            User savedUser = userRepository.save(user);
            System.out.println("stored in db");

            emailService.sendWelcomeEmail(savedUser.getUserName(), savedUser.getEmail());
            System.out.println("sent email");

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto("201", "User registered successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Registration failed: "));
        }
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                String token = UUID.randomUUID().toString();
                PasswordResetToken resetToken = new PasswordResetToken();
                resetToken.setToken(token);
                resetToken.setEmail(email);
                resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
                resetToken.setUsed(false);

                passwordResetTokenRepository.save(resetToken);

                // Send email with reset link
                emailService.sendPasswordResetEmail(email, token);

                return ResponseEntity.ok(new ResponseDto("200", "Password reset email sent"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDto("404", "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Failed to send reset email: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> resetPassword(String token, String newPassword) {
        try {
            Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
            
            if (tokenOptional.isPresent()) {
                PasswordResetToken resetToken = tokenOptional.get();
                
                if (resetToken.isUsed() || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDto("400", "Invalid or expired token"));
                }

                Optional<User> userOptional = userRepository.findByEmail(resetToken.getEmail());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);

                    resetToken.setUsed(true);
                    passwordResetTokenRepository.save(resetToken);

                    return ResponseEntity.ok(new ResponseDto("200", "Password reset successfully"));
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDto("404", "User not found"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto("400", "Invalid token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Password reset failed: " + e.getMessage()));
        }
    }
}

