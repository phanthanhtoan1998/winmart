package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.userservice.dto.request.CreateUserRequest;
import com.winmart.userservice.dto.request.LoginRequest;
import com.winmart.userservice.dto.response.CreateUserResponse;
import com.winmart.userservice.dto.response.LoginResponse;
import com.winmart.userservice.entity.UserEntity;
import com.winmart.userservice.repository.UserRepository;
import com.winmart.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, CreateUserResponse> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, UserEntity.class, CreateUserResponse.class);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(CreateUserRequest request) {
        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public ResponseEntity<CreateUserResponse> register(CreateUserRequest request) {
        try {
            // Check if user already exists
            Optional<UserEntity> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            // Create new user
            UserEntity userEntity = modelMapper.map(request, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            UserEntity savedUser = userRepository.save(userEntity);

            CreateUserResponse response = modelMapper.map(savedUser, CreateUserResponse.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        try {
            // Find user by email
            Optional<UserEntity> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            UserEntity user = userOptional.get();

            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().build();
            }

            // Create login response
            LoginResponse response = LoginResponse.builder()
                    .userId(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .role(user.getRole().name())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public CreateUserResponse saveFromObject(Object data) {
        try {
            // Convert Object to DTO
            CreateUserResponse dto;
            if (data instanceof java.util.Map) {
                dto = objectMapper.convertValue(data, CreateUserResponse.class);
            } else if (data instanceof CreateUserResponse) {
                dto = (CreateUserResponse) data;
            } else {
                dto = objectMapper.convertValue(data, CreateUserResponse.class);
            }
            
            // Convert to Entity
            UserEntity entity = modelMapper.map(dto, UserEntity.class);
            
            // Handle password encoding
            if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
                // Check if password is already encoded (starts with $2a$ for BCrypt)
                if (!entity.getPassword().startsWith("$2a$")) {
                    entity.setPassword(passwordEncoder.encode(entity.getPassword()));
                }
            } else if (dto.getId() != null) {
                // If updating without password, keep existing password
                userRepository.findById(dto.getId()).ifPresent(existingUser -> {
                    entity.setPassword(existingUser.getPassword());
                });
            }
            
            // Save
            UserEntity savedEntity = userRepository.save(entity);
            return modelMapper.map(savedEntity, CreateUserResponse.class);
        } catch (Exception e) {
            log.error("Error saving user from object: ", e);
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveListFromObject(java.util.List<Object> dataList) {
        try {
            for (Object data : dataList) {
                // Convert Object to DTO
                CreateUserResponse dto;
                if (data instanceof java.util.Map) {
                    dto = objectMapper.convertValue(data, CreateUserResponse.class);
                } else if (data instanceof CreateUserResponse) {
                    dto = (CreateUserResponse) data;
                } else {
                    dto = objectMapper.convertValue(data, CreateUserResponse.class);
                }
                
                // Convert to Entity
                UserEntity entity = modelMapper.map(dto, UserEntity.class);
                
                // Handle password encoding
                if (entity.getPassword() != null && !entity.getPassword().isEmpty()) {
                    // Check if password is already encoded (starts with $2a$ for BCrypt)
                    if (!entity.getPassword().startsWith("$2a$")) {
                        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
                    }
                } else if (dto.getId() != null) {
                    // If updating without password, keep existing password
                    userRepository.findById(dto.getId()).ifPresent(existingUser -> {
                        entity.setPassword(existingUser.getPassword());
                    });
                }
                
                // Save
                userRepository.save(entity);
            }
        } catch (Exception e) {
            log.error("Error saving list of users from object: ", e);
            throw new RuntimeException("Error saving list of users: " + e.getMessage());
        }
    }
}
