package com.winmart.userservice.controller;

import com.winmart.common.controller.BaseController;
import com.winmart.common.dto.ResponseDto;
import com.winmart.common.exception.ResponseConfig;
import com.winmart.userservice.dto.request.CreateUserRequest;
import com.winmart.userservice.dto.request.LoginRequest;
import com.winmart.userservice.dto.response.CreateUserResponse;
import com.winmart.userservice.dto.response.LoginResponse;
import com.winmart.userservice.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<CreateUserResponse>> register(@RequestBody CreateUserRequest request) {
        ResponseEntity<CreateUserResponse> response = userService.register(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseConfig.success(response.getBody());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> login(@RequestBody LoginRequest request) {
        ResponseEntity<LoginResponse> response = userService.login(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseConfig.success(response.getBody());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request);
        return ResponseConfig.success(null);
    }
}
