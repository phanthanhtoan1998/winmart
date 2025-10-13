package com.winmart.userservice.controller;

import com.winmart.common.config.bean.Public;
import com.winmart.common.dto.ResponseDto;
import com.winmart.common.exception.ResponseConfig;
import com.winmart.userservice.dto.request.CreateUserRequest;
import com.winmart.userservice.dto.request.LoginRequest;
import com.winmart.userservice.dto.response.CreateUserResponse;
import com.winmart.userservice.dto.response.LoginResponse;
import com.winmart.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Public
    public ResponseEntity<ResponseDto<LoginResponse>> login(@RequestBody LoginRequest request) {
        ResponseEntity<LoginResponse> response = userService.login(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseConfig.success(response.getBody());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    @Public
    public ResponseEntity<ResponseDto<CreateUserResponse>> register(@RequestBody CreateUserRequest request) {
        ResponseEntity<CreateUserResponse> response = userService.register(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseConfig.success(response.getBody());
        }
        return ResponseEntity.badRequest().build();
    }
}
