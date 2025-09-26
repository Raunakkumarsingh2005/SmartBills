package com.spring.smartbills.service;

import com.spring.smartbills.dtos.LoginDto;
import com.spring.smartbills.dtos.SignUpDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> login(LoginDto loginDto);
    ResponseEntity<?> register(SignUpDto signUpDto);
    ResponseEntity<?> forgotPassword(String email);
    ResponseEntity<?> resetPassword(String token, String newPassword);
}

