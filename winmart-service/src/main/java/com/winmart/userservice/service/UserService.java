package com.winmart.userservice.service;

import com.winmart.common.service.BaseService;
import com.winmart.userservice.dto.request.CreateUserRequest;
import com.winmart.userservice.dto.request.LoginRequest;
import com.winmart.userservice.dto.response.CreateUserResponse;
import com.winmart.userservice.dto.response.LoginResponse;
import com.winmart.userservice.dto.response.UserProfileResponse;
import com.winmart.userservice.entity.UserEntity;
import org.springframework.http.ResponseEntity;

public interface UserService extends BaseService<UserEntity, CreateUserResponse> {
    void createUser(CreateUserRequest request);

    ResponseEntity<CreateUserResponse> register(CreateUserRequest request);

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

    ResponseEntity<UserProfileResponse> getMe(String token);
    
    // Create user from customer info if not exists, then add points
    ResponseEntity<String> addPointsByPhoneOrCreateUser(String phone, String customerName, Integer points);
}
