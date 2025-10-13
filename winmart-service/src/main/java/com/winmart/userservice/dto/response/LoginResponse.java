package com.winmart.userservice.dto.response;

import com.winmart.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse extends BaseDto {
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn; // in seconds
}
