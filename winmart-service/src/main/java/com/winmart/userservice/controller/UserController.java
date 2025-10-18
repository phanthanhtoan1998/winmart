package com.winmart.userservice.controller;

import com.winmart.common.config.bean.Public;
import com.winmart.common.controller.BaseController;
import com.winmart.common.dto.ResponseDto;
import com.winmart.common.exception.ResponseConfig;
import com.winmart.userservice.dto.request.CreateUserRequest;
import com.winmart.userservice.dto.request.LoginRequest;
import com.winmart.userservice.dto.response.CreateUserResponse;
import com.winmart.userservice.dto.response.LoginResponse;
import com.winmart.userservice.dto.response.UserProfileResponse;
import com.winmart.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Public
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<CreateUserResponse>> register(@Valid @RequestBody CreateUserRequest request) {
        ResponseEntity<CreateUserResponse> response = userService.register(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseConfig.success(response.getBody(), "Registration successful");
        }
        return ResponseConfig.badRequest("Registration failed");
    }

    @Public
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        ResponseEntity<LoginResponse> response = userService.login(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseConfig.success(response.getBody(), "Login successful");
        }
        return ResponseConfig.badRequest("Invalid email or password");
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(request);
        return ResponseConfig.success(null, "User created successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserProfileResponse>> getMe(@RequestHeader("Authorization") String token) {
        ResponseEntity<UserProfileResponse> response = userService.getMe(token);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseConfig.success(response.getBody(), "User profile retrieved successfully");
        }
        return ResponseConfig.badRequest("Invalid token or user not found");
    }
}
