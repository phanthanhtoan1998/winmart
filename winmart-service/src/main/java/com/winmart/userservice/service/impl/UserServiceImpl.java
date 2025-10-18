package com.winmart.userservice.service.impl;

import com.winmart.common.service.impl.BaseServiceImpl;
import com.winmart.common.util.JwtUtil;
import com.winmart.userservice.dto.request.CreateUserRequest;
import com.winmart.userservice.dto.request.LoginRequest;
import com.winmart.userservice.dto.response.CreateUserResponse;
import com.winmart.userservice.dto.response.LoginResponse;
import com.winmart.userservice.dto.response.UserProfileResponse;
import com.winmart.userservice.entity.UserEntity;
import com.winmart.userservice.repository.UserRepository;
import com.winmart.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, CreateUserResponse> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private Long jwtExpiration;
    
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        super(userRepository, UserEntity.class, CreateUserResponse.class);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
                log.warn("Registration failed: Email already exists - {}", request.getEmail());
                return ResponseEntity.badRequest().build();
            }

            // Create new user
            UserEntity userEntity = modelMapper.map(request, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            
            // Set role if not provided
            if (userEntity.getRole() == null) {
                userEntity.setRole(UserEntity.UserRole.CUSTOMER);
            }
            
            UserEntity savedUser = userRepository.save(userEntity);
            log.info("User registered successfully: {}", savedUser.getEmail());

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
                log.warn("Login failed: User not found - {}", loginRequest.getEmail());
                return ResponseEntity.badRequest().build();
            }

            UserEntity user = userOptional.get();

            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                log.warn("Login failed: Invalid password for user - {}", loginRequest.getEmail());
                return ResponseEntity.badRequest().build();
            }

            // Generate JWT tokens
            String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail());

            // Create login response
            LoginResponse response = LoginResponse.builder()
                    .userId(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .role(user.getRole().name())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtExpiration / 1000) // Convert to seconds
                    .build();

            log.info("User logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<UserProfileResponse> getMe(String token) {
        try {
            // Remove "Bearer " prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Validate token and extract user info
            if (!jwtUtil.validateToken(token)) {
                log.warn("Get me failed: Invalid token");
                return ResponseEntity.badRequest().build();
            }

            // Extract user ID from token
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                log.warn("Get me failed: Could not extract user ID from token");
                return ResponseEntity.badRequest().build();
            }

            // Find user by ID
            Optional<UserEntity> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                log.warn("Get me failed: User not found with ID - {}", userId);
                return ResponseEntity.badRequest().build();
            }

            UserEntity user = userOptional.get();

            // Create user profile response
            UserProfileResponse response = UserProfileResponse.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .address(user.getAddress())
                    .role(user.getRole())
                    .createdAt(user.getCreatedAt())
                    .build();

            log.info("User profile retrieved successfully: {}", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Get me error: {}", e.getMessage(), e);
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
